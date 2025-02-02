<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>To-Do List</title>
    <link href="../css/styles.css" rel="stylesheet">
</head>
<body>

<div class="container">
    <div class="header">
        <h1>Welcome, <%=request.getSession().getAttribute("username")%>!</h1>
        <div class="header-buttons">
            <form action="logout" method="POST">
                <button type="submit" class="btn logout-btn">Logout</button>
            </form>
        </div>
    </div>

    <form id="task-form" class="task-form">
        <label>
            <input type="text" class="task-input" id="task-input" placeholder="Enter your task" required>
        </label>
        <label>
            <input type="text" class="task-description" id="task-description" placeholder="Enter description">
        </label>
        <button type="submit" class="btn add-btn">Add Task</button>
    </form>

    <h2 class="task-list-title">Task List</h2>
    <ul id="task-list" class="task-list"></ul>

    <p id="empty-message" class="empty-task-msg" style="display: none;">There are no active tasks!</p>
</div>

<script>
    let tasks = [];

    <% if (request.getAttribute("tasks") != null) { %>
    tasks = <%= request.getAttribute("tasks") %>;
    <% } %>

    document.addEventListener('DOMContentLoaded', function () {
        renderTasks(tasks);
    });

    function renderTasks(tasks) {
        const taskListElement = document.getElementById('task-list');
        taskListElement.innerHTML = '';

        if (tasks.length === 0) {
            document.getElementById('empty-message').style.display = 'block';
        } else {
            document.getElementById('empty-message').style.display = 'none';

            tasks.forEach(task => {
                const taskItem = document.createElement('li');
                taskItem.classList.add('task-item');

                const taskText = document.createElement('span');
                taskText.classList.add('task-text');
                taskText.textContent = task.title;

                const taskDescription = document.createElement('p');
                taskDescription.classList.add('task-text-description');
                taskDescription.textContent = task.description ? task.description : "No description";

                const deleteButton = document.createElement('button');
                deleteButton.classList.add('btn', 'delete-btn');
                deleteButton.textContent = 'Delete';
                deleteButton.onclick = () => deleteTask(task.id);

                const taskActions = document.createElement('div');
                taskActions.classList.add('task-actions');
                taskActions.appendChild(deleteButton);

                taskItem.appendChild(taskText);
                taskItem.appendChild(taskDescription);
                taskItem.appendChild(taskActions);

                taskListElement.appendChild(taskItem);
            });
        }
    }

    function loadTasks() {
        fetch('/to-do')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to load tasks');
                }
                return response.json();
            })
            .then(tasks => {
                renderTasks(tasks);
            })
            .catch(error => console.error('Error loading tasks:', error));
    }

    document.getElementById('task-form').addEventListener('submit', function (event) {
        event.preventDefault();
        const taskInput = document.getElementById('task-input');
        const taskDescription = document.getElementById('task-description');
        const newTask = {
            title: taskInput.value,
            description: taskDescription.value
        };

        fetch('/to-do', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: `task=${encodeURIComponent(newTask.title)}&description=${encodeURIComponent(newTask.description)}`
        })
            .then(response => response.json())
            .then(data => {
                tasks = data;
                taskInput.value = '';
                taskDescription.value = '';
                renderTasks(tasks);
            })
            .catch(error => console.error('Error adding task:', error));
    });

    function deleteTask(taskId) {
        fetch(`/to-do?deletedTask=${encodeURIComponent(taskId)}`, {
            method: 'DELETE'
        })
            .then(response => response.json())
            .then(updatedTasks => {
                renderTasks(updatedTasks);
            })
            .catch(error => console.error('Error deleting task:', error));
    }
</script>

</body>
</html>
