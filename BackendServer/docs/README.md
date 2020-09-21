# Backend API server for Damn Vulnerable Bank

This application starts an API server that serve incoming requests from the android application.

## Installation

- Install dependencies
    - `nodejs (v10.19.0)`
    - `npm (6.14.4)`
    - `mysql (Ver 8.0.21-0ubuntu0.20.04.4 for Linux on x86_64 ((Ubuntu)))`
- Update mysql configuration (username, password fields) in `development` object `config/config.json`
- Populate database with data
    - `cat database/schema+data.sql | mysql -u root -p`
- Install npm packages
    - `npm install`
- Start the application server
    - `npm start`
- Make request to `/api/health/check` to check the status
