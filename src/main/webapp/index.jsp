<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Player List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">

<div class="container mt-4">

    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3 class="fw-bold">Player List</h3>
        <a href="player?action=add" class="btn btn-primary">
            + Add Player
        </a>
    </div>

    <div class="card shadow-sm">
        <div class="card-body">

            <c:if test="${empty players}">
                <div class="alert alert-info">
                    No player found.
                </div>
            </c:if>

            <c:if test="${not empty players}">
                <table class="table table-bordered table-hover align-middle">
                    <thead class="table-dark">
                    <tr>
                        <th>Name</th>
                        <th>Full Name</th>
                        <th>Age</th>
                        <th>Index</th>
                        <th style="width: 160px">Action</th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach items="${players}" var="p">
                        <tr>
                            <td>${p.name}</td>
                            <td>${p.fullName}</td>
                            <td>${p.age}</td>
                            <td>
                                <span class="badge bg-info text-dark">
                                        ${p.indexName}
                                </span>
                            </td>
                            <td>
                                <a href="player?action=edit&id=${p.playerId}"
                                   class="btn btn-warning btn-sm">
                                    Edit
                                </a>

                                <a href="player?action=delete&id=${p.playerId}"
                                   class="btn btn-danger btn-sm"
                                   onclick="return confirm('Delete this player?')">
                                    Delete
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>

        </div>
    </div>

</div>

</body>
</html>
