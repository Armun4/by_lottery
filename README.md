# Lottery App

## Overview

The Lottery App is a simple application designed to facilitate daily lotteries. It allows participants to register, 
submit ballots, and participate in daily lotteries.
The app automatically creates a new lottery for each day if one hasn't been created yet. A
t midnight, the app selects a random ballot from the submitted entries for today's lottery and saves the winner ballot.
Additionally, it prepares a new lottery for the following day.

## Main Flow

1. **App Initialization**: Upon starting the application, the `LotteryInitializer` component checks if a lottery exists for the current day. If not, it creates a new lottery.

2. **Participant Registration**: Participants can register with the app by providing their name and optionally, their email address.

3. **Ballot Submission**: Registered participants can submit their ballots for the current day's lottery. Each ballot represents an entry into the lottery.

4. **Lottery Selection**: At midnight, the `LotteryTask` component randomly selects a ballot from the submitted entries for today's lottery.

5. **New Lottery Preparation**: Following the lottery selection, the app prepares a new lottery for the next day.

## Usage

To use the Lottery App:
- Ensure the application is running.
- Participants can register through the `/v1/participant/register` endpoint.
- Once registered, participants can submit their ballots through the `/v1/ballot/submit` endpoint.
- The app automatically handles the selection of winners and prepares new lotteries for the following day.
- Winning ballots can always be consulted through `/v1/ballot/winner` endpoint for any day.

## Technologies Used

- Java
- Spring Boot
- PostgreSQL (for data storage)



## Endpoints

The application consists of three main endpoints:

1. `/v1/participant/register`: This endpoint allows new participants to register in the lottery system by providing their name and email address. Authentication via JWT token is required.

2. `/v1/ballot/submit`: Registered participants can use this endpoint to submit their ballots for the lottery. They need to provide their participant ID and the number of ballots they wish to submit.

3. `/v1/ballot/winner`: This endpoint retrieves the winning ballot for a specific date. Participants can query the winning ballot for a particular date by providing the date parameter in the request.


## Endpoints in detail

###  `/v1/participant/register`
- **HTTP Method**: `POST`
- **Description**: Registers a new participant.

#### Request
- **Body**: `ParticipantRequest` JSON object containing participant persona information.
  ```json
  {
    "name": string,
    "email": string
  }
  
#### Response
- **Status Codes**:
  - `200 OK`: Successfully registered the participant.
  - `400 Bad Request`: Invalid request parameters.

- **Body**: JSON object representing the registered participant.
  ```json
  {
    "id": long,
    "name": string,
    "email": string
  }
  

### `/v1/ballot/submit`
- **HTTP Method**: `POST`
- **Description**: Submits ballots for a participant.

#### Request
- **Body**: `BallotSubmitRequest` JSON object containing participant ID and amount of ballots.
  ```json
  {
    "participantId": integer,
    "amount": integer
  }
  
####  Response
- **Status Codes**:
    - `200 OK`: Successfully submitted ballots.
    - `400 Bad Request`: Invalid request parameters or the ticket is submitted for a non-existing participant.

- **Body**: JSON array of `BallotResponse` objects.
  ```json
  [
    {
      "participantId": integer,
      "amount": integer,
      "loterryId": integer
    },
    ...
  ]

### `/v1/ballot/winner`
- **HTTP Method**: `GET`
- **Description**: Retrieves the winning ballot for a specific date.

#### Request Parameters
- `date` (required): The date for which to retrieve the winning ballot. (Format: `YYYY-MM-DD`)

#### Response
- **Status Codes**:
  - `200 OK`: Successfully retrieved the winning ballot.
  - `400 Bad Request`: Invalid request parameters.
  - `401 No Content`: Winning ballot not found for a given date.


- **Body**: JSON object representing the winning ballot.
  ```json
  {
    "ballotId": long,
    "lotteryId": long,
    "participantId": long,
    "participantName": string,
    "winningDate": string (ISO 8601 date format: YYYY-MM-DD)
  }