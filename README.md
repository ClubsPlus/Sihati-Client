# Sihati-Client
And android app that can be used as a covid status Sanitary Pass, it employs a color scheme to determine the health status of the user, which allows us to identify the uninfected and permits them to access the University. 

It consists of parts: User, and Laboratory, which will be synchronized with one another, each one with its own application, and this is the Client side.

While the user will receive a notification every week to take a test, the status will be changed to "Not tested" status, Then he has to take an appointment in one of the laboratories listed based on their schedule, While each laboratory can make his schedule with a limited number of people in each phase, and each time the laboratory edit or delete any schedule, all the users signed up in that schedule will receive a notification to inform them, once he makes the test, His status will be changed to "Pending" status, and when the result is ready the laboratory upload the result and the user will get a notification informing that his results are ready and the state will automatically update for the user, In case the user is positive the administration will directly receive an email that that student/teacher is contaminated and the status change to "Positive" with the number of quarantine days required. 
Assuredly. 

# The Main Functions Are To Be Realized By The Actor
1- User
  .Create account
  .Log in
  .Change password
  .See the current status
  .Get a notification when it is time to take a test 
  .See the history of tests
  .make an appointment for a test in one of the laboratories schedules available
  Cancel an appointment
2- Laboratory
  .Create account
  .Log in
  .Create a schedule 
  .Edit a schedule
  .Delete a schedule
  .See the list of people for each schedule
  .Set the person state from not tested to pending
  .Set the result for the pending person
  .Edit the result of a test
  .See tests history
  

