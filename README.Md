# Covid Tracker 

## About 
Contact Tracing Android application developed in March-May 2020. The goal is to improve on manual contact tracing that suffers from limitations related to limited ability for a person to recall or recognize everyone they have been in contact with. 
 The application was developed on **Android Studio** using **Java** combined with a **MongoDB** cloud instance for the database. 

## Contributors
This application was developed by Hala Saadeh and Elias Azar. 
### Contact Info
<halagsaadeh@gmail.com>
 <ea1ik.pro@gmail.com>

## How does it work?
Assume a person goes about their daily life, interacting with people at work, the bus, etc... A few days later, they test positive for COVID-19. Everyone who was near them, within a certain radius, during the last 14 days receives a notification stating "You have recently been in contact with someone who has tested positive with COVID19" along with steps on what to do next. 
 The app's aim is to alleviate some of the issues that come with manual contact tracing for the virus. A COVID positive person may not be able to recall exactly all the people they interacted with closely. Moreover, they may not directly know who those people are. For example, if a person stands in a crowded supermarket, they may not be able to identify every single one of their contacts.
 The app attempts to solve this issue by **anonymously** keeping track of a user's contacts. 

## Features
### User Anonymity 
The vast majority of users' biggest concern is their privacy and security. This was reported in a user feedback survey sent out during the development of the application.
 Anonymity was the centerpoint around which the application was designed and developed. No external authentication service was used. User contacts were stored as anonymous alphanumeric IDs.
 In order to maintain a way to save user data, should a user choose to switch their mobile device, users were given the option to create a username and password, *NOT* related to their emails. The username and password are completely anonymous.

### Contact Storing
Contacts are repeatedly updated for each user. Depending on a user's location, people within a 5 meter radius are marked as "contacts" for this user. For every contact, their ID and contact date is stored. The date enables us to discard all contacts that occured more than 14 days before. The date is always updated to store the latest day of contact.

### Medical Authorization
 Only a medical professional can set a user's COVID status as positive. This is implemented to avoid potential abuse to the application by users who falsely set themselves as positive and create a chain of scares among their contacts. Therefore, the app is designed to work hand in hand with the Ministry of Health. Medical professionals who work in COVID19 testing centers would be provided with unique alphanumeric codes which they have to input in order to set a user's status as positive.
 This way, we can better preserve the integrity of the application and mitigate potential false scares.

## Screenshots of the application
To be added. 

## Promotional Poster
To be added. 

## Feedback Form 
During the development of the application, users were sent a survey. The survey included COVID19 related questions, general phone/security questions, and app-specific questions. 
 The survey had two language options: English and Arabic.
 160 people submitted responses to the survey anonymously. 
 Survey results can be found in /survey_results.Md
