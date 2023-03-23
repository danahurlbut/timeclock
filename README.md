Welcome to my Simple Time Clock app! Here is what you need to know to be able to log in and use it:

Unforunately I did not have the time to develop an entire API along with the Android app. After trying a few workarounds, I worked around this by finding a free api that was somewhat close to what I needed (https://rapidapi.com/NiKHiLkr23/api/uers-api/). I created an account to use this API and modified the data to make most aspects of functionality work. In areas where the API did not have the functionality needed (such as creating and modifying shifts), I wrote out the code I would use if I did have a fully functional API and made comments where appropriate in the code. I apologize if any of this seems hacky in anyway and regret that I did not have the time to develop an API. This API does seem to be prone to the occasional socket timeout for the login call so that may take a few tries sometimes.

In order to test out all the functionalities, use these username and password combinations:

Admin User - username: rec_cf6loej7fe2hse8uio6g password: wordpass
Non-Admin User starting a shift - username: rec_cf6lo3p2f302eh72ocn0 password: wordpass
Non-Admin User ending a shift - username: rec_cf6lol7k2id522jo5m3g password: wordpass

Overall I was able to develop most of the functionalities suggested. See comments in code for enhancements I would make given more time -- I would be happy to expand upon the implementation of any of these in our interview. Thank you! Dana