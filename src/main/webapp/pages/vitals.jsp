<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vital Measures - HealthData Manager</title>
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

        .card h2, .card h3 {
            color: var(--primary);
            margin-bottom: 20px;
            font-size: 1.5rem;
            display: flex;
            align-items: center;
            gap: 10px;
            border-bottom: 1px solid #eee;
            padding-bottom: 10px;
        }

        .card h3 {
            font-size: 1.3rem;
            color: var(--dark);
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

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .stat-card {
            background: var(--white);
            border-radius: var(--radius);
            box-shadow: var(--shadow);
            padding: 20px;
            text-align: center;
            transition: transform 0.3s ease;
        }

        .stat-card:hover {
            transform: translateY(-5px);
        }

        .stat-value {
            font-size: 2rem;
            font-weight: 700;
            color: var(--primary);
            margin-bottom: 5px;
        }

        .stat-label {
            color: var(--gray);
            font-size: 0.9rem;
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

        footer {
            text-align: center;
            margin-top: 50px;
            padding: 20px;
            color: var(--gray);
            font-size: 0.9rem;
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
        }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<div class="container">
    <header>
        <h1><i class="fas fa-heartbeat"></i> Vital Measures Management</h1>
        <p>Add, view and analyze patient health data</p>
    </header>

    <!-- Back to Home button at the top -->
    <div style="text-align: center; margin: 20px 0;">
        <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-back">
            <i class="fas fa-arrow-left"></i> Back to Home
        </a>
    </div>

    <div style="text-align: center; margin: 20px 0;">
        <a href="vitals" class="btn ${empty mode ? 'btn-primary' : 'btn-secondary'}">All Measurements</a>
        <a href="vitals?mode=stats" class="btn ${mode == 'stats' ? 'btn-primary' : 'btn-secondary'}">Statistics</a>
        <a href="vitals?mode=anomalies" class="btn ${mode == 'anomalies' ? 'btn-danger' : 'btn-secondary'}">Anomalies</a>

    </div>
    <c:if test="${mode == 'anomalies'}">
        <div style="background-color: #fdecea; color: #c0392b; padding: 15px; border-radius: 8px; margin-bottom: 20px;">
            ⚠ Showing all measurements outside of normal range.
        </div>
    </c:if>

    <c:choose>
        <c:when test="${mode == 'stats'}">
            <div class="card">
                <h2><i class="fas fa-filter"></i> Filter by Patient</h2>
                <form method="get" action="vitals" class="form">
                    <input type="hidden" name="mode" value="stats">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="patientFilter">Select Patient</label>
                            <select id="patientFilter" name="patientId" class="form-control">
                                <option value="">All Patients</option>
                                <c:forEach var="p" items="${patients}">
                                    <option value="${p.id}" ${param.patientId == p.id ? 'selected' : ''}>${p.nom}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-search"></i> Apply Filter
                    </button>
                </form>
            </div>

            <div class="section-title">
                <i class="fas fa-chart-bar"></i>
                <h3>Measurement Statistics</h3>
            </div>

            <div class="stats-grid">
                <c:forEach var="s" items="${stats}">
                    <div class="stat-card">
                        <!-- Format average value -->
                        <div class="stat-value">
                            <fmt:formatNumber value="${s[1]}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                        </div>

                        <div class="stat-label">${s[0]} (average)</div>

                        <div style="margin-top: 10px; font-size: 0.8rem; color: var(--gray);">
                            Min: <fmt:formatNumber value="${s[2]}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                            | Max: <fmt:formatNumber value="${s[3]}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                        </div>
                    </div>
                </c:forEach>
            </div>


        </c:when>

        <c:when test="${mode == 'anomalies'}">
            <div class="section-title">
                <i class="fas fa-exclamation-triangle"></i>
                <h3>Detected Anomalies</h3>
            </div>

            <div class="card">
                <table>
                    <thead>
                    <tr>
                        <th>Patient</th>
                        <th>Type</th>
                        <th>Value</th>
                        <th>Date</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="v" items="${vitals}">
                        <tr>
                            <td>${v.patient.nom}</td>
                            <td>${v.typeMesure}</td>
                            <td>${v.valeur}</td>
                            <td>${v.dateMesure}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:when>

        <c:otherwise>
            <div class="card">
                <h2><i class="fas fa-plus-circle"></i> Add a Measurement</h2>
                <form method="post" action="vitals" class="form">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="typeMesure">Measurement Type</label>
                            <input type="text" id="typeMesure" name="typeMesure" class="form-control" placeholder="Ex: Blood Pressure, Blood Sugar..." required>
                        </div>

                        <div class="form-group">
                            <label for="valeur">Value</label>
                            <input type="number" id="valeur" name="valeur" step="0.01" class="form-control" placeholder="Ex: 12.5" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="patientId">Patient</label>
                        <select id="patientId" name="patientId" class="form-control" required>
                            <option value="" disabled selected>Select a patient</option>
                            <c:forEach var="p" items="${patients}">
                                <option value="${p.id}">${p.nom}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <button type="submit" class="btn btn-primary btn-block">
                        <i class="fas fa-save"></i> Save Measurement
                    </button>
                </form>
            </div>
            <div class="card">
                <h2><i class="fas fa-filter"></i> Filter by Patient</h2>
                <form method="get" action="vitals" class="form">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="patientFilter">Select Patient</label>
                            <select id="patientFilter" name="patientId" class="form-control">
                                <option value="">All Patients</option>
                                <c:forEach var="p" items="${patients}">
                                    <option value="${p.id}" ${param.patientId == p.id ? 'selected' : ''}>${p.nom}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-search"></i> Apply Filter
                    </button>
                </form>
            </div>



            <div class="section-title">
                <i class="fas fa-table"></i>
                <h3>All Measurements</h3>
            </div>

            <div class="card">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Type</th>
                        <th>Value</th>
                        <th>Date</th>
                        <th>Patient</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="v" items="${vitals}">
                        <tr>
                            <td>${v.id}</td>
                            <td>${v.typeMesure}</td>
                            <td>${v.valeur}</td>
                            <td>${v.dateMesure}</td>
                            <td>${v.patient.nom}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>



    <div style="text-align: center; margin-top: 30px;">
        <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-back">
            <i class="fas fa-arrow-left"></i> Back to Home
        </a>
    </div>

    <footer>
        <p>HealthData Manager &copy; 2023 - Health Data Management System</p>
    </footer>
</div>
</body>
</html>