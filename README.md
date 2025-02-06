The goal of this task is to create simple REST API  which allows users to manage TODOs. 
The API should allow to create/delete/update TODOs and categories as well as search for user, name, description, deadline and category in any combination. *For example find all todos for an user X where deadline is today and name contains test.* 
The API should also implement basic authorization/authentication: *User X cannot access TODOs of user Y as long as he doesn't have admin role.*

You are free to use any library or testing framework in the project.

Below you may find a proposition of the DB model:

![DB model](DBModel.png)

Users can be created using /login/create or /admin/create with this JSON body:
```
{
    "username":"admin",
    "password":"admin"
}
```
*For testing purposes, the authorisation for admins has been removed, so admins can be created*

All user can view task categories, but only admins can create, edit, or delete them.
All authenticated users can create, edit, and delete tasks belonging to them.
Administrators can view all available tasks, but normal users can only view their own.

You can search for tasks with specific criteria with the endpoint /tasks/search using the TaskFilter schema:

![image](https://github.com/user-attachments/assets/949766a7-02d3-4d2f-a105-be3aca2b739f)
```
{
            "taskName": "Admin2 task",
            "taskDescription": "",
            "deadlineFrom":"2025-02-04",
            "deadlineTo":"2025-02-07",
            "categoryId":1,
            "owner": {
                "id": 1
            }
}
```

API endpoints can be fouund via /swagger-ui/index.html

![image](https://github.com/user-attachments/assets/03313c74-8d5c-4135-a968-4372fa55c6d8)
