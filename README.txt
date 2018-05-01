What this project currently does:
    - Takes in user input (Street, Street 2, City, State, Zip)
    - Submits user user input to /search
    - Backend extracts form data and makes a get request to the Democracy Works API
    - Receives the response and displays it to the user

What I would add with more time
    - Tests!
    - Ensure there is better error handling on both the front-end, by ensuring required fields
      are not left blank and on the backend, by having exception handling in place.
    - Augmenting the data by deriving the county from the zipcode and adding that 
      to the data passed to the Democracy Works API
    - Split the views into different files. This is especially useful when a project grows in size
    - Spend more time on making the UI nicer.
