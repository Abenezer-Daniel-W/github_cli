# Github events CLI

A simple github cli project to look at recent github event, original idea from [roadmap.sh](https://roadmap.sh/projects/task-tracker).

## 🚀 Features
pass in username and see recent github events like
- Issue creation 
- Recent commits
- Creation of repos

## 📦 Prerequisites

- Java JDK 17+
- Maven 3.6+
- Git (optional, if you need to clone the repo)

## 🔧 Build Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/Abenezer-Daniel-W/github_cli.git
   cd github_cli
   ```

2. **Build the project using Maven**
   ```bash
   mvn clean compile
   ```

3. **Package the project into a `.jar` file**
   ```bash
   mvn package
   ```

   The generated JAR file will be located in the `target/` directory.

## ▶️ Run the Application

### Option 1: Run the Compiled jar

```bash
java -cp target/github_action_cli-1.0-SNAPSHOT-jar-with-dependencies.jar com.example.Main "someUsername"
```

## 🛠 Project Structure

```
├── pom.xml
├── README.md
├── src
    ├── main
    │   ├── java
    │   └── resources
    └── test
        └── java

```


