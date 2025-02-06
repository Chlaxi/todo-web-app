create table if not exists task_categories (
    category_id INTEGER PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
    category_name VARCHAR2(100) NOT NULL UNIQUE,
    category_description VARCHAR2(500)
);

create table if not exists users (
    id INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
    username VARCHAR2(100) NOT NULL UNIQUE,
    password VARCHAR2(500) NOT NULL,
    roles VARCHAR2(100) NOT NULL
);


create table if not exists tasks (
    task_id INTEGER PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
    task_name VARCHAR2(100) NOT NULL,
    task_description VARCHAR2(500),
    deadline timestamp NOT NULL,
    category_id INTEGER DEFAULT 1,
    owner_id INTEGER NOT NULL,
    FOREIGN KEY (category_id) REFERENCES task_categories(category_id),
    FOREIGN KEY (owner_id) REFERENCES users(id)
);