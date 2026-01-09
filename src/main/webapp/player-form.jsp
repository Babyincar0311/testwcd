<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Player Form</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">
<div class="container mt-4" style="max-width: 600px">

    <div class="card shadow-sm">
        <div class="card-header bg-primary text-white">
            <h5 class="mb-0">
                ${player == null ? "Add New Player" : "Edit Player"}
            </h5>
        </div>

        <div class="card-body">
            <form method="post" action="player">
                <input type="hidden" name="playerId" value="${player.playerId}">

                <!-- Name -->
                <div class="mb-3">
                    <label class="form-label">Name</label>
                    <input class="form-control" name="name"
                           value="${player.name}" placeholder="Enter name">
                </div>

                <!-- Full name -->
                <div class="mb-3">
                    <label class="form-label">Full Name</label>
                    <input class="form-control" name="fullName"
                           value="${player.fullName}" placeholder="Enter full name">
                </div>

                <!-- Age -->
                <div class="mb-3">
                    <label class="form-label">Age</label>
                    <input class="form-control" name="age"
                           value="${player.age}" placeholder="Enter age">
                </div>

                <!-- Index -->
                <div class="mb-3">
                    <label class="form-label">Index</label>
                    <select class="form-select" name="indexId">
                        <c:forEach items="${indexers}" var="i">
                            <option value="${i.indexId}"
                                ${player.indexId == i.indexId ? "selected" : ""}>
                                    ${i.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="mb-3">
                    <label class="form-label">Index Value</label>
                    <input type="number" step="any" class="form-control"
                           name="indexValue"
                           value="${playerIndex.value}"
                           placeholder="Enter index value">

                    <small class="text-muted">
                        Please enter value in allowed range
                    </small>
                </div>

                <!-- Buttons -->
                <div class="d-flex justify-content-between">
                    <button class="btn btn-success">Save</button>
                    <a href="player" class="btn btn-secondary">Back</a>
                </div>
            </form>

            <!-- Error message -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger mt-3">
                        ${error}
                </div>
            </c:if>
        </div>
    </div>

</div>
</body>
</html>
