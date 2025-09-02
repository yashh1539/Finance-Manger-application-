Project Title: Personal Finance Manager

🧾 Overview:
A Personal Finance Manager is a desktop application that helps users track their income, expenses, savings, and budgets. Users can input transactions, categorize them, view monthly reports, and set financial goals.

🔧 Tech Stack:
•	Frontend: Java Swing (for GUI)
•	Backend/Logic: Core Java
•	Database: MySQL 
•	Optional: JasperReports for printable reports, JFreeChart for visual analytics

✅ Features:
🏠 Dashboard
•	Overview of current balance, total expenses, income, and savings
💸 Expense/Income Tracker
•	Add/Edit/Delete transactions
•	Categories (e.g., Food, Rent, Travel, Salary)
📊 Reports
•	Monthly/Yearly expense summary
•	Charts: Pie chart for category-wise expense, line graph for monthly trends
📅 Budget Planning
•	Set monthly budgets per category
•	Warning/alerts when budget limits are exceeded
🔐 User Management (Optional)
•	Simple login system to store user-specific data
📁 Data Export
•	Export reports to PDF/CSV


🗃️ Database: finance_manager
________________________________________
1. Users
Stores basic user login credentials.
Field	Type	Description
user_id	INT (PK)	Auto-incremented ID
username	VARCHAR(50)	Unique username
password	VARCHAR(255)	Hashed password
email	VARCHAR(100)	Optional contact email
________________________________________
2. Transactions
Stores all income and expense records.
Field	Type	Description
transaction_id	INT (PK)	Auto-incremented ID
user_id	INT (FK)	Links to Users
date	DATE	Date of the transaction
type	VARCHAR(10)	'Income' or 'Expense'
category	VARCHAR(50)	Category like Food, Rent, etc.
amount	DECIMAL(10,2)	Amount of transaction
note	TEXT	Optional description
________________________________________
3. Budgets
Stores monthly budget per category.
Field	Type	Description
budget_id	INT (PK)	Auto-incremented ID
user_id	INT (FK)	Links to Users
month	VARCHAR(10)	Format: '2025-04'
category	VARCHAR(50)	Budget category
limit_amount	DECIMAL(10,2)	Budget limit
________________________________________
4. Goals (Optional Table for financial goals)
Field	Type	Description
goal_id	INT (PK)	Auto-incremented ID
user_id	INT (FK)	Links to Users
name	VARCHAR(100)	Goal title (e.g. Buy Laptop)
target_amount	DECIMAL(10,2)	Target saving amount
saved_amount	DECIMAL(10,2)	Current progress
deadline	DATE	Optional deadline











