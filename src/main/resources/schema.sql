create table if not exists task_categories (
    category_id INTEGER PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
    category_name VARCHAR2(100) NOT NULL UNIQUE,
    category_description VARCHAR2(500)
);

create table if not exists tasks (
    task_id INTEGER PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
    task_name VARCHAR2(100) NOT NULL,
    task_description VARCHAR2(500),
    deadline timestamp NOT NULL,
    category_id INTEGER DEFAULT 1,
    FOREIGN KEY (category_id) REFERENCES task_categories(category_id)
);