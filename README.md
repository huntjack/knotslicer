# Project Description

Do you struggle to schedule team meetings for coding side projects? Do you hate micromanaging your schedule with traditional calendar apps?<br>

Spend more time doing the things that you love by simplifying the timeboxing process. Things don't always go as planned. It's difficult to predict 
what you're going to be doing at any given time. Instead of focusing on scheduling every little thing that you need to do, why not focus on when you are sure 
you will be free? This streamlines the timeboxing step. You only need to think about when you will actually be available for a meeting.

Once you have established when you're available. Knotslicer will handle the rest! You can create projects associated with your team members. You can create events 
and polls for those events, so that you can find a schedule that suits everyone. Knotslicer makes this easy by helping you find available meeting times based on 
the attendees schedules.

# Setup

1. First of all, you will need JDK 11, Maven, and Docker Desktop (Windows/MacOS) or Docker engine(Linux) installed on your computer to run this appplication server.

2. Prepare a "docker-compose.yml" file. Add passwords required in "docker-compose-template.yml" and change filename from "docker-compose-template.yml" to "docker-compose.yml"

3. In the root directory($PATH/knotslicer/), run:

```
mvn clean package
docker build -t knotslicer:1.0 .
docker compose -f docker-compose.yml up
```

# How can I use it?

You can create users, projects, events, and polls for your events using HTTP methods. In order to add another user to your project,
you need to create a member that represents the user in the project. Projects, events and members are owned by a single user. This user 
effectively controls all of the entities that it owns. Some exceptions are made for members. The project or event owner controls which 
members are added to or removed from a project or event. When creating a project or event that you are a part of, remember to add your 
corresponding member to it. Each member indicates available time by creating a series of schedules that represent the member's free time. 
After creating an event, the event owner should invite guests by adding their members to the newly created event. If you can't attend, 
simply remove your member from the event. Once you have everyone in the event, you may now create polls for your attendees. Members can 
answer polls by creating a poll answer. 

Knotslicer will help you find the perfect time using its Find Available Event Times feature. You can set a minimum meeting time using the 
query parameter "minimum". This value is in minutes. The default value that must be passed by the client if no other value is specified is 
fifteen. The "minimum" query parameter cannot be greater than 960.

# URI and JSON Templates

Use these URI and JSON templates as guidelines when using this project. Naturally, the domain may be different in your case, but for the
sake of example, "localhost" will be used. For POST and PATCH requests, there should be a header with the key "Content-Type" and the value
"application/json" and a JSON request body similar to the one described below. The JSON request bodies will be presented in raw JSON form. 
When performing a PATCH request, do not omit or set any values to null, unless you intend to change it to null. The exception 
to this rule would be the key-value pairs that are included in the note section below each request body template.

### User
User POST URI Template: `http://localhost:8080/users`
User GET/PATCH/DELETE URI Template: `http://localhost:8080/users/{userId}`
User Request Body Template:

```
{
    "email": "example01@email.com",
    "userName": "John",
    "userDescription": "John's User Description",
    "timeZone": "America/Los_Angeles"
}
```

Note: For PATCH requests, you should omit the "email" key-value pair.

User's Projects URI Template: `http://localhost:8080/users/{userId}/projects`
HTTP Request Method: GET

User's Events URI Template: `http://localhost:8080/users/{userId}/events`
HTTP Request Method: GET

User's Members URI Template: `http://localhost:8080/users/{userId}/members`
HTTP Request Method: GET

### Project
Project POST URI Template: `http://localhost:8080/projects`
Project GET/PATCH/DELETE URI Template: `http://localhost:8080/projects/{projectId}` 
Project Reqest Body Template:

```
{
    "userId": 1,
    "projectName": "John's Project",
    "projectDescription": "Project Description 1"
}
```

Note: The "userId" key-value pair represents the user that owns the project. It should be omitted for PATCH requests. Projects ownership 
cannot be transferred at this time. If you wish to change the owner of a project, you can simply recreate the project in the meantime.

Project's Members URI Template: `http://localhost:8080/projects/{projectId}/members`
HTTP Request Method: GET

### Member
Member POST URI Template: `http://localhost:8080/members`
Member GET/PATCH/DELETE URI Template: `http://localhost:8080/members/{memberId}`
Member Request Body Template:

```
{ 
    "userId": 1,
    "projectId": 1,
    "name": "John",
    "role": "Back-end Developer",
    "roleDescription": "Back-end Developer Role Description"
}
```

Note: The "userId" key-value pair represents the user that owns the member. The "projectId" key-value pair represents that project that the member belongs to. 
Both of these key-value pairs should be omitted for Patch Requests.

Member's Schedules URI Template: `http://localhost:8080/members/{memberId}/schedules`
HTTP Request Method: GET

Member's Events URI Template: `http://localhost:8080/members/{memberId}/events`
HTTP Request Method: GET

### Schedule
Schedule POST URI Template: `http://localhost:8080/members/{memberId}/schedules`
Schedule GET/PATCH/DELETE URI Template: `http://localhost:8080/members/{memberId}/schedules/{scheduleId}`
Schedule Request Body Template:
 
```
{ 
    "startTimeUtc": "2024-12-19T19:00:00.000",
    "endTimeUtc": "2024-12-19T23:00:00.000"
}
```

### Event
Event POST URI Template: `http://localhost:8080/events`
Event GET/PATCH/DELETE URI Template: `http://localhost:8080/events/{eventId}`
Event Request Body Template:
 
```
{
    "userId": 1,
    "subject": "Subject 1",
    "eventName": "Event 1",
    "eventDescription": "Event 1 Description"
}
```

Note: The "userId" key-value pair represents that event owner. It should be omitted for PATCH requests.

Add Member to Event URI Template: `http://localhost:8080/events/{eventId}/members`
HTTP Request Method: PATCH
Add Member Request Body Template:
 
```
{
    "memberId": 1
}
```

Remove Member from Event URI Template: `http://localhost:8080/events/{eventId}/members/{memberId}`
HTTP Request Method: DELETE

Event's Polls URI Template: `http://localhost:8080/events/{eventId}/polls`
HTTP Request Method: GET

Event's Members URI Template: `http://localhost:8080/events/{eventId}/members`
HTTP Request Method: GET

Available Event Times URI Template: `http://localhost:8080/events/{eventId}/availabletimes?minimum={minimum-meeting-time-in-minutes}`
Client Side Default Minimum Value: 15
Server Side Maximum Minimum Value: 960
HTTP Request Method: GET

### Poll
Poll POST URI Template: `http://localhost:8080/polls`
Poll GET/PATCH/DELETE URI Template: `http://localhost:8080/polls/{pollId}`
Poll Request Body Template:
 
```
{
    "eventId": 1,
    "startTimeUtc": "2023-12-19T05:00:00.000",
    "endTimeUtc": "2023-12-19T08:00:00.000"
}
```

Note: The "eventId" key-value pair represents this poll's event. It should be omitted for PATCH requests.

Poll's PollAnswers URI Template: `http://localhost:8080/polls/{pollId}/pollanswers`
HTTP Request Method: GET

### Poll Answers
Poll Answer POST URI Template: `http://localhost:8080/polls/{pollId}/pollanswers`
Poll Answer GET/PATCH/DELETE URI Template: `http://localhost:8080/polls/{pollId}/pollanswers/{pollAnswerId}`
Poll Answer Request Body Template:
 
```
{
    "memberId": 1,
    "approved": true
}
```

Note: The "memberId" key-value pair represents the member that this Poll Answer belongs to. It should be omitted for PATCH requests.
