# School of Computing &mdash; Year 4 Project Proposal Form

## SECTION A

|                     |                   |
|---------------------|-------------------|
|Project Title:       | NFC Powered DCU   |
|Student 1 Name:      | Eamon Crawford    |
|Student 1 ID:        | 16437394          |
|Student 2 Name:      | Sam O’Leary       |
|Student 2 ID:        | 16358763          |
|Project Supervisor:  | Renaat Verbruggen |

## SECTION B

> Guidance: This document is expected to be approximately 3 pages in length, but it can exceed this page limit.
> It is also permissible to carry forward content from this proposal to your later documents (e.g. functional
> specification) as appropriate.
>
> Your proposal must include *at least* the following sections.


### Introduction

Our project seeks to empower users with real time and historic data which will be pulled from user interactions with NFC devices. The purpose of such devices will vary but they will always offer some benefit to the end user.

> Describe the general area covered by the project.

The general area covered by this project is server setup and management, app development, database setup and management, and data manipulation.

Server: As our project aims to be a system spread out throughout an area of the campus we will connect it all to a server to manage the information flow. We will use Docker to simplify the process of deployment, allowing the system to be easily set-up in new locations (as required).

NFC: The project will be able to identify nfc tags and accurately record specific IDs to allow correct authorization of amenity usage. We will be building our code reader in the form of a Kotlin Android app. It will have the ability to both read and write tags, however in a final build this would be separated so that the end user does not have the capability to write their own tags.

Database: Information generated by the project will be stored in a created database for general display usages , as well as analytical uses. This will be achieved through the use of SQL.

Data manipulation: Any data we store in the database will be available for data manipulation. This will primarily be done in python due to the convenience of packages such as pandas and numpy. Expansion in this area is one of our stretch goals but is not within the minimum viable product we have identified.
on in this area is one of our stretch goals but is not within the minimum viable product we have identified.



> Outline the proposed project.

The proposed project aims to provide automated management of amenities in the interest of availability and security. This goal will be achieved through utilizing NFCto restrict access to these amenities to authorized users, allowing us to additionally track which amenities are currently being accessed. The information generated by the system will be stored in a connected server and utilised to visually display availability of amenities to the end user while also building a historic usage log for general analysis with the interest of improving management of amenities, tracking the depreciation of assets, and predicting maintenance requirements/costs. This would be achieved through the attachment of NFC sensors to doors either doors or computers, by implementing it this way we could additionally ease user access (eliminating the need to type username and password) and also allow for better attendance taking for things such as tutorials or lab exams.
In the case of our chosen use case; DCU computing labs and study rooms, the system would allow the university to accurately display lab availability to students, while also allowing them to identify peak usage hours for allotment of project labs.

> Where did the ideas come from?

Sam raised the idea as a nfc tool for companies HR departments to be able to use data driven planning back by data analysis, Eamon, having previously worked with NFC technology, was keen to see how much further He could push it, although we were both concerns are GDPR and the lack of a focus point for the project. In our first meeting with Mr. Verbruggen, it became clear that focusing on how our idea could be applied to DCU, gave us a massive advantage as we could easily identify areas where there could be a clear benefit for the staff and students.

### Achievements

> What functions will the project provide? Who will the users be?

For our project we will be using DCU as our use case. We will set out to provide real time information in areas such as car park spaces, computer lab availability, library study rooms availability and more. We also plan to provide administration with historical data representation.  Our system should also be able to predict near future conditions.

Due to GDPR we will have to split users into those who have opted in and agree to have their data be used and will have access to all features of the system, and those who haven’t not opted in and will not have any identifiable data saved, thus stopping them from using the system for certain use cases.

Our main source of data will come from NFC devices we will build that can cover such tasks as logging a user into their lab computer, scanning in for parking more reliably, checking into library study rooms or the student hub pods, marking attendance for exams, tutorials, and lectures. We could automatically log users into being on campus when they present at an nfc device, instead of them manually checking in using the current app, although it may be harder to automatically check users out of being on campus without them voluntarily doing so at a nfc device built for that task. Users will interact with these nfc devices through a new nfc enabled student card, or a mobile phone app. The smart phone app will be how we opt in users for the GDPR dependent features, and therefore the app will be optional for those who do not want to opt in.

The modular set up of features built on top of simple NFC readers gives our project plenty of room for expansion by future projects or developments by the staff or students.


### Justification

> Why/when/where/how will it be useful?

Providing administration with historical data, gives them more insight into how students and other members of the college use the facilities, therefore allowing them to do more data driven planning.

Our real time information systems will allow users to save time and not unnecessarily disturb others by manually checking.

Our NFC devices should give users more ease of use when interacting with the university's facilities.

Using the NFC functionality of the new student cards when pulling student information will reduce overhead caused by human error, and NFC is more reliable than using the current barcode system.

With Covid-19 being such a large problem currently, the benefit of partially tracking users movements in high risk areas, gives admin the tools to better handle contact tracing with less manual work and risk of human error.

### Programming language(s)

> List the proposed language(s) to be used.

* Kotlin.
* React (any web apps).
* React Native (smart phone apps).


### Programming tools / Tech stack

> Describe the compiler, database, web server, etc., and any other software tools you plan to use.

* Redbrick Servers.
* Docker (easy deployment to servers).
* MySql / Postgresql.
* Compiler.
* Intellij.
* VsCode.
* DataGrip.

### Hardware

> Describe any non-standard hardware components which will be required.

* Servers.
* NFC cards (NXP MIFARE Ultralight EV1 Chip for cards, others for RFID stickers).
* Android Phone.
* A Windows Desktop computer for development.



### Learning Challenges

> List the main new things (technologies, languages, tools, etc) that you will have to learn.

* Data analysis.
* Setting up a database.
* Deploying a server.
* Learning docker.
* Encryption challenges.
* Learning React/ React native.
* Learning Kotlin.


### Breakdown of work

> Clearly identify who will undertake which parts of the project.
>
> It must be clear from the explanation of this breakdown of work both that each student is responsible for
> separate, clearly-defined tasks, and that those responsibilities substantially cover all of the work required
> for the project.

* Database set up
* Server deployment
* Api development
* Backend
* Mobile app development
* Nfc reader code
* Data analysis
* Front end for data representation (web app / mobile)

#### Student 1
Eamon will work on the backend, data analysis, server deployment, database set up and api development. I have a lot of experience with front end and therefore wanna do something outside of my comfort zone.

#### Student 2
Sam will work on the frontend, Data manipulation, Data visualization, and general user interfaces. My weakest area of development is the front end side of development and as such I want to challenge myself to improve my knowledge and skills using this project.

#### Glossary
Near Field Communication (NFC) : is a set of communication protocols for communication between two electronic devices within a close vicinity of each other.