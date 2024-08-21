# Android Compose TODO App

## Description
This is an Android Compose application that I created as a front end for my [Task Manager Api](https://github.com/xaawii/task-manager-api).

In this app, you can register and log in to the API, and use the password recovery system, which will send a security code to your email that you will need to enter in the app.
You can also create, update, and delete tasks for any day, and see all your tasks for each day on the scrollable calendar that I created.

## How to Use
Since I haven't published the app or the backend, you'll need to follow these steps:
* Make sure Docker is configured and running on your computer.
* Run the Docker Compose file mentioned in the `README.md` of my [Task Manager Api](https://github.com/xaawii/task-manager-api).
* Download and open this project in Android Studio.
* Add the following line to your `local.properties` file: `API_BASE_URL=` with the base URL of the API (if the API is running in Docker and you are using the Android Studio emulator, you can use `10.0.2.2` as the host, and if you're using a physical device, use your computer's IP address).
