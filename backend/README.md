# Backend Documentation

## Project Overview
This is the backend component of the application, built with Flask and MySQL.

## Prerequisites
- Python 3.x
- MySQL Server

## Installation

1. Install the required Python packages:
   ```bash
   pip install -r requierements.txt
   ```

2. Configure the database connection:
   - Open `database.py`
   - Replace the placeholder values in the `connect()` method (lines 45-52):
     ```python
     self.connection = mysql.connector.connect(
         host="YOUR_HOST", 
         database="YOUR_DATABASE_NAME",
         user="YOUR_USERNAME",
         password="YOUR_PASSWORD",
         autocommit=True,
         pool_reset_session=True
     )
     ```
   - Replace:
     - `YOUR_HOST` with your MySQL host (usually `localhost`)
     - `YOUR_DATABASE_NAME` with your database name
     - `YOUR_USERNAME` with your MySQL username
     - `YOUR_PASSWORD` with your MySQL password

3. Ensure your MySQL server is running and the specified database exists.

## Running the Application

To start the Flask development server:

```bash
python app.py
```

The server will run on `http://localhost:5000` by default.

## Project Structure
- `app.py` - Main Flask application entry point
- `database.py` - Database connection handler (contains the <<>> placeholders to configure)
- `models/` - Database models
- `*_routes.py` - Route handlers for different entities
- `requierements.txt` - Python dependencies

## API Endpoints
The API endpoints are defined in the route files:
- `visiteur_routes.py` - Visiteur-related endpoints
- `visiter_routes.py` - Visiter-related endpoints
- `site_routes.py` - Site-related endpoints

## Notes
- The application uses Flask-CORS to allow requests from frontend applications
- Database connections are managed through a singleton pattern in `Database` class
- Connection pooling and thread safety are implemented