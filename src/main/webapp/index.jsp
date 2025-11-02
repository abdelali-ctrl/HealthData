<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HealthData Manager</title>
    <style>
        :root {
            --primary: #3498db;
            --primary-dark: #2980b9;
            --secondary: #2ecc71;
            --secondary-dark: #27ae60;
            --dark: #2c3e50;
            --light: #ecf0f1;
            --danger: #e74c3c;
            --warning: #f39c12;
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
            padding: 30px 0;
            border-radius: var(--radius);
            box-shadow: var(--shadow);
            margin-bottom: 30px;
            text-align: center;
        }

        header h1 {
            font-size: 2.5rem;
            margin-bottom: 10px;
            font-weight: 600;
        }

        header p {
            font-size: 1.1rem;
            opacity: 0.9;
        }

        .dashboard {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 25px;
            margin-bottom: 40px;
        }

        .card {
            background: var(--white);
            border-radius: var(--radius);
            box-shadow: var(--shadow);
            padding: 20px;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }


        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.12);
        }

        .card h2 {
            color: var(--primary);
            margin-bottom: 15px;
            font-size: 1.5rem;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .card h2 i {
            font-size: 1.3rem;
        }

        .menu-list {
            list-style: none;
        }

        .menu-list li {
            margin-bottom: 12px;
        }

        .menu-list a {
            display: flex;
            align-items: center;
            padding: 12px 15px;
            background-color: var(--light);
            border-radius: var(--radius);
            text-decoration: none;
            color: var(--dark);
            font-weight: 500;
            transition: all 0.3s ease;
        }

        .menu-list a:hover {
            background-color: var(--primary);
            color: var(--white);
            transform: translateX(5px);
        }

        .menu-list a i {
            margin-right: 10px;
            font-size: 1.2rem;
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

        .stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-top: 30px;
        }

        .stat-card {
            background: var(--white);
            border-radius: var(--radius);
            box-shadow: var(--shadow);
            padding: 20px;
            text-align: center;
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

        footer {
            text-align: center;
            margin-top: 50px;
            padding: 20px;
            color: var(--gray);
            font-size: 0.9rem;
        }

        .card-description {
            color: var(--gray);
            margin-bottom: 15px;
            font-size: 0.95rem;
        }

        .compact-card {
            min-height: 200px;
        }

        .compact-card .menu-list {
            margin-top: 10px;
        }

        .compact-card .card-description {
            margin-bottom: 10px;
        }

        @media (max-width: 768px) {
            .dashboard {
                grid-template-columns: 1fr;
            }

            .form-row {
                grid-template-columns: 1fr;
            }

            header h1 {
                font-size: 2rem;
            }
        }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<div class="container">
    <header>
        <h1><i class="fas fa-heartbeat"></i> HealthData Manager</h1>
        <p>Centralized management of patients health data</p>
    </header>

    <div class="dashboard">
        <div class="card compact-card">
            <h2><i class="fas fa-users"></i> Patient Management</h2>
            <p class="card-description">Manage patient records, demographics, and medical history</p>
            <ul class="menu-list">
                <li><a href="patients"><i class="fas fa-list"></i> Patient List</a></li>
            </ul>
        </div>

        <div class="card compact-card">
            <h2><i class="fas fa-chart-line"></i> Measurements & Statistics</h2>
            <p class="card-description">Track and analyze patient vital signs and health metrics</p>
            <ul class="menu-list">
                <li><a href="vitals"><i class="fas fa-heart"></i> Vital Measurements</a></li>
            </ul>
        </div>

        <div class="card">
            <h2><i class="fas fa-database"></i> Data Generation</h2>
            <p>Generate large-scale test data for the system</p>

            <form method="post" action="generate" class="form">
                <div class="form-group">
                    <label for="nomPatient">Patient Name</label>
                    <input type="text" id="nomPatient" name="nomPatient" class="form-control" value="PatientBatch" required>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="age">Age</label>
                        <input type="number" id="age" name="age" class="form-control" value="35" required>
                    </div>

                    <div class="form-group">
                        <label for="sexe">Gender</label>
                        <select id="sexe" name="sexe" class="form-control">
                            <option>M</option>
                            <option>F</option>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label for="n">Number of Measurements</label>
                    <input type="number" id="n" name="n" class="form-control" value="10000" required>
                </div>

                <button type="submit" class="btn btn-primary btn-block">
                    <i class="fas fa-bolt"></i> Generate Data
                </button>
            </form>
        </div>
    </div>


    <footer>
        <p>HealthData Manager &copy; 2023 - Health data management system</p>
    </footer>
</div>
</body>
</html>