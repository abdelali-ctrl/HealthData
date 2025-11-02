<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Patient Management - HealthData Manager</title>
    <style>
        :root {
            --primary: #3498db;
            --primary-dark: #2980b9;
            --secondary: #2ecc71;
            --secondary-dark: #27ae60;
            --danger: #e74c3c;
            --warning: #f39c12;
            --dark: #2c3e50;
            --light: #ecf0f1;
            --gray: #95a5a6;
            --white: #ffffff;
            --shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            --radius: 8px;
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            background-color: #f5f7fa;
            color: var(--dark);
            line-height: 1.6;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        header {
            background: linear-gradient(135deg, var(--primary), var(--primary-dark));
            color: var(--white);
            padding: 25px 0;
            border-radius: var(--radius);
            box-shadow: var(--shadow);
            margin-bottom: 30px;
            text-align: center;
        }

        header h1 {
            font-size: 2.2rem;
            margin-bottom: 10px;
            font-weight: 600;
        }

        header p {
            font-size: 1.1rem;
            opacity: 0.9;
        }

        .card {
            background: var(--white);
            border-radius: var(--radius);
            box-shadow: var(--shadow);
            padding: 25px;
            margin-bottom: 30px;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .card:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
        }

        .card h2 {
            color: var(--primary);
            margin-bottom: 20px;
            font-size: 1.5rem;
            display: flex;
            align-items: center;
            gap: 10px;
            border-bottom: 1px solid #eee;
            padding-bottom: 10px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 500;
            color: var(--dark);
        }

        .form-control {
            width: 100%;
            padding: 12px 15px;
            border: 1px solid #ddd;
            border-radius: var(--radius);
            font-size: 1rem;
            transition: border 0.3s ease, box-shadow 0.3s ease;
        }

        .form-control:focus {
            outline: none;
            border-color: var(--primary);
            box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.2);
        }

        .form-row {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 15px;
        }

        .btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 12px 25px;
            background-color: var(--secondary);
            color: var(--white);
            border: none;
            border-radius: var(--radius);
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            gap: 8px;
        }

        .btn:hover {
            background-color: var(--secondary-dark);
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
        }

        .btn-primary {
            background-color: var(--primary);
        }

        .btn-primary:hover {
            background-color: var(--primary-dark);
        }

        .btn-block {
            width: 100%;
        }

        .btn-back {
            background-color: var(--gray);
        }

        .btn-back:hover {
            background-color: #7f8c8d;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
            box-shadow: var(--shadow);
            border-radius: var(--radius);
            overflow: hidden;
        }

        th, td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #eee;
        }

        th {
            background-color: var(--primary);
            color: var(--white);
            font-weight: 600;
        }

        tr:nth-child(even) {
            background-color: #f8f9fa;
        }

        tr:hover {
            background-color: #e8f4fc;
        }

        .section-title {
            display: flex;
            align-items: center;
            margin: 30px 0 20px 0;
            color: var(--dark);
            font-size: 1.4rem;
        }

        .section-title i {
            margin-right: 10px;
            color: var(--primary);
        }

        .patient-count {
            background: var(--white);
            border-radius: var(--radius);
            box-shadow: var(--shadow);
            padding: 15px;
            text-align: center;
            margin-bottom: 20px;
        }

        .count-value {
            font-size: 2rem;
            font-weight: 700;
            color: var(--primary);
            margin-bottom: 5px;
        }

        .count-label {
            color: var(--gray);
            font-size: 0.9rem;
        }

        footer {
            text-align: center;
            margin-top: 50px;
            padding: 20px;
            color: var(--gray);
            font-size: 0.9rem;
        }

        .action-buttons {
            display: flex;
            gap: 10px;
            margin-top: 10px;
        }

        .btn-small {
            padding: 8px 15px;
            font-size: 0.9rem;
        }

        .btn-edit {
            background-color: var(--warning);
        }

        .btn-edit:hover {
            background-color: #e67e22;
        }

        .btn-delete {
            background-color: var(--danger);
        }

        .btn-delete:hover {
            background-color: #c0392b;
        }

        .alert {
            padding: 15px;
            border-radius: var(--radius);
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .alert-error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .search-container {
            margin-bottom: 20px;
        }

        .search-box {
            display: flex;
            gap: 10px;
        }

        .search-input {
            flex-grow: 1;
        }

        .empty-state {
            text-align: center;
            padding: 40px 20px;
            color: var(--gray);
        }

        .empty-state i {
            font-size: 3rem;
            margin-bottom: 15px;
            color: #ddd;
        }

        @media (max-width: 768px) {
            .form-row {
                grid-template-columns: 1fr;
            }

            header h1 {
                font-size: 1.8rem;
            }

            th, td {
                padding: 8px 10px;
                font-size: 0.9rem;
            }

            .action-buttons {
                flex-direction: column;
            }

            .search-box {
                flex-direction: column;
            }
        }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<div class="container">
    <header>
        <h1><i class="fas fa-users"></i> Patient Management</h1>
        <p>Add, view and manage patient information</p>
    </header>

    <div style="text-align: center; margin: 20px 0;">
        <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-back">
            <i class="fas fa-arrow-left"></i> Back to Home
        </a>
    </div>

    <!-- Success/Error Messages -->
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success">
            <i class="fas fa-check-circle"></i> ${successMessage}
        </div>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-error">
            <i class="fas fa-exclamation-circle"></i> ${errorMessage}
        </div>
    </c:if>

    <div class="card">
        <c:choose>
            <c:when test="${not empty patientToEdit}">
                <h2><i class="fas fa-user-edit"></i> Edit Patient (${patientToEdit.nom})</h2>
            </c:when>
            <c:otherwise>
                <h2><i class="fas fa-user-plus"></i> Add New Patient</h2>
            </c:otherwise>
        </c:choose>

        <form method="post" action="patients" class="form">

            <c:if test="${not empty patientToEdit}">
                <input type="hidden" name="patientId" value="${patientToEdit.id}">
            </c:if>

            <div class="form-group">
                <label for="nom">Full Name</label>
                <input type="text" id="nom" name="nom" class="form-control"
                       value="${patientToEdit.nom}"
                       placeholder="Enter patient's full name" required>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="age">Age</label>
                    <input type="number" id="age" name="age" class="form-control"
                           value="${patientToEdit.age}"
                           placeholder="Enter age" min="0" max="150" required>
                </div>

                <div class="form-group">
                    <label for="sexe">Gender</label>
                    <select id="sexe" name="sexe" class="form-control" required>
                        <option value="" disabled ${empty patientToEdit ? 'selected' : ''}>Select gender</option>
                        <option value="H" ${patientToEdit.sexe == 'H' ? 'selected' : ''}>Male</option>
                        <option value="F" ${patientToEdit.sexe == 'F' ? 'selected' : ''}>Female</option>
                    </select>
                </div>
            </div>



            <button type="submit" class="btn btn-primary btn-block">
                <c:choose>
                    <c:when test="${not empty patientToEdit}">
                        <i class="fas fa-save"></i> Update Patient
                    </c:when>
                    <c:otherwise>
                        <i class="fas fa-plus"></i> Add Patient
                    </c:otherwise>
                </c:choose>
            </button>

            <c:if test="${not empty patientToEdit}">
                <a href="patients" class="btn btn-back btn-block" style="margin-top: 10px;">
                    <i class="fas fa-times"></i> Cancel Edit
                </a>
            </c:if>
        </form>
    </div>

    <div class="patient-count">
        <div class="count-value">${patients.size()}</div>
        <div class="count-label">Total Patients Registered</div>
    </div>

    <div class="section-title">
        <i class="fas fa-list"></i>
        <h3>Patient List</h3>
    </div>

    <!-- Search Functionality -->
    <div class="search-container">
        <form method="get" action="patients" class="search-box">
            <input type="text" name="search" class="form-control search-input"
                   placeholder="Search patients by name..." value="${param.search}">
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-search"></i> Search
            </button>
            <c:if test="${not empty param.search}">
                <a href="patients" class="btn btn-back">
                    <i class="fas fa-times"></i> Clear
                </a>
            </c:if>
        </form>
    </div>

    <div class="card">
        <c:choose>
            <c:when test="${not empty patients}">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Age</th>
                        <th>Gender</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="p" items="${patients}">
                        <tr>
                            <td>${p.id}</td>
                            <td>${p.nom}</td>
                            <td>${p.age}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${p.sexe == 'H'}">Male</c:when>
                                    <c:when test="${p.sexe == 'F'}">Female</c:when>
                                    <c:otherwise>${p.sexe}</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div class="action-buttons">
                                    <a href="patients?editId=${p.id}"
                                       class="btn btn-edit btn-small">
                                        <i class="fas fa-pencil-alt"></i> Edit
                                    </a>

                                    <a href="patients?deleteId=${p.id}"
                                       class="btn btn-delete btn-small"
                                       onclick="return confirm('Are you sure you want to delete patient: ${p.nom}?');">
                                        <i class="fas fa-trash"></i> Delete
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <i class="fas fa-user-injured"></i>
                    <h3>No Patients Found</h3>
                    <p>There are no patients in the system yet. Add your first patient using the form above.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <div style="text-align: center; margin-top: 30px;">
        <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-back">
            <i class="fas fa-arrow-left"></i> Back to Home
        </a>
    </div>

    <footer>
        <p>HealthData Manager &copy; 2023 - Health Data Management System</p>
    </footer>
</div>

<script>
    // Client-side form validation
    document.addEventListener('DOMContentLoaded', function() {
        const form = document.querySelector('form');

        form.addEventListener('submit', function(event) {
            let isValid = true;
            const nameInput = document.getElementById('nom');
            const ageInput = document.getElementById('age');

            // Name validation
            if (nameInput.value.trim().length < 2) {
                alert('Please enter a valid name (at least 2 characters)');
                nameInput.focus();
                isValid = false;
            }

            // Age validation
            if (ageInput.value < 0 || ageInput.value > 150) {
                alert('Please enter a valid age (0-150)');
                ageInput.focus();
                isValid = false;
            }

            if (!isValid) {
                event.preventDefault();
            }
        });
    });
</script>
</body>
</html>