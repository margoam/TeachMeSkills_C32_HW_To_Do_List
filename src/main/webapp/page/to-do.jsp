<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

    <form action="to-do" method="POST" class="task-form">
        <label>
            <input type="text" class="task-input" name="task" placeholder="Enter your task" required>
        </label>
        <button type="submit" class="btn add-btn">Add Task</button>
    </form>

    <h2 class="task-list-title">Task List</h2>

    <c:if test="${tasks != null || !tasks.isEmpty()}">
        <ul class="task-list">
            <c:forEach var="task" items="${tasks}">
                <li class="task-item">
                    <span class="task-text">${task.title}</span>
                    <form action="to-do" method="POST" class="delete-form">
                        <input type="hidden" name="deletedTask" value="${task.title}">
                        <button type="submit" class="btn delete-btn">Delete</button>
                    </form>
                </li>
            </c:forEach>
        </ul>
    </c:if>

    <c:if test="${tasks == null || tasks.isEmpty()}}">
        <p class="empty-task-msg">There are no active tasks!</p>
    </c:if>

    <c:if test="${tasks != null || !tasks.isEmpty()}">
        <ul class="task-list">
            <c:forEach var="task" items="${tasks}">
                <li class="task-item">
                    <span class="task-text">${task.title}</span>
                    <form action="to-do" method="POST" class="delete-form">
                        <input type="hidden" name="deletedTask" value="${task.title}">
                        <button type="submit" class="btn delete-btn">Delete</button>
                    </form>
                </li>
            </c:forEach>
        </ul>
    </c:if>
</div>

</body>
</html>
