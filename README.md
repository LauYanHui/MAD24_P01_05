## App Name: CookBuddy
![image](https://github.com/user-attachments/assets/7a5555f5-1a78-4daf-825d-8857d8c4b30c)

## App Category: Food & Drink
## What is CookBuddy?
Your go-to app for culinary inspiration and healthy eating! With our app, you will be able to explore a wide variety of recipes and culinary technique tutorials suitable for all skill levels. Whether you're craving comfort food or seeking lighter options, Cooking Companion provides delicious recipes tailored to your taste buds and health goals. Download our app and discover the joy of cooking nutritious meals that nourishes your body and delights your senses!

## App Objective:

As food connoisseurs, we understand that there are some of them that are willing to try recreating good food themselves to consume. However, individuals like us aren't aware of the paramount difficulty of making good food, causing them to lose motivation to 
begin their culinary journey. Therefore, with the help of our app, not only will beginner chefs be inspired to start cooking, but chefs from all skill levels will also be inspired to view the variety of recipes that they could try and cook for themselves. This app provides recipes from all kinds of cuisines, providing people a single-app solution when trying to search for recipes to make.

## App Colour Theme:

![image](https://github.com/user-attachments/assets/3b3f6059-1dbc-4f05-b863-d72288133444)

1. Visual Clarity and Focus: The light grey background helps the colorful food images and text stand out, making it easier for users to focus on the content without being distracted by a busy or overly vibrant background.
2. Modern and Clean Aesthetic: The use of neutral tones like grey is often associated with modern and minimalistic design. It gives the app a clean and professional look, which can be appealing to users who prefer simplicity.


## User's Guide:
1. A new user wants to register a new account in the 'CookBuddy' app.
   1. Enter the 'Register' page from the 'Login' page.
   2. In the 'Register' page, key in a gmail address and a strong password.
   3. A verification email will be sent to that Gmail address.
   4. Click on the link that is attached to the verification email.
   5. Enter back into 'CookBuddy' to input in a username to complete the registration progress.
   6. Upon registration, future Login processes will be automatically completed.
   
2. A novice chef wants to learn more about 'Deglazing' in order to widen his variety of recipes he/she can try replicating.
   1. Sign-in/Register into 'CookBuddy' app.
   2. From the navigation bar, click on the 2nd navigation item (left to right).
   3. A recycler list of Culinary Techniques should be displayed.
   4. Click on the 'Deglazing' Card.
   5. Finally, details of the technique should be displayed, giving the user more information alongside a short video tutorial.
   6. For better user experience, the user can also click on the "Fullscreen" or "Speed Control" button.

3. A novice chef wants to get recipes to cook.
   1. Sign-in/Register into 'CookBuddy' app.
   2. A recycler in recycler where the parent recycler that consist of main ingredients and the child recycler would show the recipes.
   3. click on one of the recipe that interests the user.
   4. details about the recipe would be displayed such as the main ingredient, cuisine, nutrition facts, ingredients and instructions will all be shown.
   5. on the instruction fragment, the user can slide the viewpager to the left to see more instructions
   6. if the user want there is a text to speech button located below the viewpager, it would read the viewpager's contents out loud.
   7. there is also a timer feature in build where the user can set a timer for up to an hour that will play an alarm and send a notification when the alarm ends

4. A chef wants to keep track of groceries to buy.
   1. Sign-in/Register into 'CookBuddy' app.
   2. From the navigation bar, click on the 3rd navigation item (left to right).
   3. Currently, an empty list should be displayed.
   4. For each grocery item a user wants to keep track of, they can add each item in manually by inputting in the name of the grocery item.
   5. The grocery items will then get displayed in the grocery list.
   6. For each grocery item, there is a checkbox on each item, giving users a more easy way of removing multiple grocery items at once the moment they order their items.
   

## Stage 1:

1. Account Creation/Login(Ryan)
   - This feature will ensure that users are provided a secure and personalized experience when trying to make use of our app. When they create an account, they will be provided a list of checkboxes that indicate certain food allergies before being able to view recipes and culinary techniques that they want to learn.


2. A Scroll Display of Recipes:(Yan Hui)
   -  This feature is tailored towards all users trying to find unique recipes to make good food with. This feature will provide users on the various recipes we have on our app. These recipes will be displayed in a visually appealing, scrollable format, providing culinary inspiration to users. When they click on a certain recipe, they will be given a detailed run-down of the recipe, providing users with the list of ingredients required, along with nutritional information for each information like the amount of calories, protein, carbs and fats. On top of that, they will be provided a horizontal scrollable format of the instructions for the recipe, meaning that they can simply scroll to the right to move on to the next instruction.


3. Dietary Restrictions(Jun Hao):
   - This feature is tailored towards users that may have certain dietary restrictions like food allergies. In this case, upon account registration, users will be able to key in their 'said' food restriction.

4. Grocery List (Shan Chun)
   - When users look for recipes on our app, they may have get overwhelmed by what ingredients they have to purchase. Typically, they will use a "Notes" app in order to keep track of that. However, in our app, we provide an in-built grocery list such that it provides them with a 'One-stop solution', meaning that they won't have to keep switching between other apps. Users will be able to manually add and delete ingredients that they require. This can overall make their shopping process a bit more streamlined.

5. Culinary Technique Tutorials (Ashton)
   - When beginner chefs are trying to follow recipes, they may end up being overwhelmed by the amount of skill required and especially if its their first time trying to replicate the technique. Therefore, with this feature, it provides them video evidence on how typical restaurant chefs manipulate such techniques in order to maximize the quality of the food product. Each technique tutorial will provide insights on the importance of this technique and how it can boosts the quality of the food product. On top of that, a video will be displayed to the users so that they can view the technique in action, done by professional chefs.

## Stage 2: 
1. Firebase Authentication (Ryan)
   - Added email and password providers for users to sign into 'CookBuddy'. By using Firebase Authentication, it ensures that the registration/sign=in processes are much more secure and protected. Upon first registration, a verification email gets sent to the gmail provided by the user, ensuring that the gmail used is a valid gmail. In 'ProfileFragment', users can edit their user information, editing their username and password. The Firebase Authentication uses a UID that can be used as a key for Firebase Realtime Database.
2. Text to speech, Recycler in Recycler, Timer (Yan Hui)
   - added text to speech so that when users are cooking all they need to do is press one button for the instructions to be read out. Improved recipe UI by using recycler in recyler where the parent recycler show the main ingredient a recipe used and the child recycler just shows all the recipes that has the main ingredient. In the recipe app, I’ve implemented a timer feature that enhances user experience by providing dual notifications when the timer concludes. It utilizes both an audible alarm and a visual notification to ensure users are promptly alerted when their cooking time is up, making it easier to keep track of their recipes without missing a beat.

3. Food Detector (Jun Hao)
   - When a user wants to search for a recipe (ie. click on the search bar and key in at least one character), a camera icon will appear, allowing users to get pictures from their phone's local storage to detect food ingredients. With these ingredients, food recipes will be filtered and displayed.

4. Location Detector (Shan Chun)
   - This feature uses Google Maps API to look for certain supermarkets nearby the user. This allows our users to figure out which supermarket is most convenient for them in search of their grocery items.

6. Optimized Video Tutorials (Ashton)
   - Swapped out general videoView provided via Android Studio to ExoPlayer's PlayerView. Along with the PlayerView includes a more dynamic layout to allow for a better user experience. From customizing their speed control to scrubbing through the video, it ensures that their experience with the video tutorial is much more smoother.

## App Screenshots:
![image](https://github.com/user-attachments/assets/cfd86ae9-5959-4a2b-ba35-1188ae184551)
![image](https://github.com/user-attachments/assets/5dff1e89-6e88-4ad5-b6b9-49c98733d9ee)
![image](https://github.com/user-attachments/assets/6aa81284-d524-47ca-a4fe-8189562bf27f)
![image](https://github.com/user-attachments/assets/57d19b9f-1159-41f2-a7dd-c978dd253af4)
![image](https://github.com/user-attachments/assets/bda8c675-3a4f-4e8c-a903-99cc4cc35f12)

# References: 
### Video References:
   - [How to Braise Meat | Tesco](https://www.youtube.com/watch?v=TyEhYBWDV2o)
   - [Pan Frying](https://www.youtube.com/watch?v=C5fG8NTbXso)
   - [Simple Steaming Method](https://www.youtube.com/watch?v=URlzwxDNieI)
   - [How to Deglaze a pan](https://www.youtube.com/watch?v=E1IdzidpwUE)
   - [Cooking Techniques: How to Poach Fish](https://www.youtube.com/watch?v=29dbc18zmkg)
### Image References:
   - [Mushroom risotto](https://www.allrecipes.com/recipe/85389/gourmet-mushroom-risotto/)
   - [mexican corn salad](https://www.loveandlemons.com/mexican-street-corn-salad/)
   - [barbeque chicken](https://www.savorynothings.com/bbq-chicken/)
   - [pad thai](https://www.recipetineats.com/chicken-pad-thai/)
   - [greek salad](https://downshiftology.com/recipes/greek-salad/)
   - [spanish tapas](https://www.goya.com/en/recipes/spanish-tapas-platter)
   - [sweet and sour chicken](https://omnivorescookbook.com/air-fryer-sweet-and-sour-chicken/)
   - [quiche lorraine](https://www.allrecipes.com/recipe/17515/quiche-lorraine-i/)
   - [muhammara](https://vikalinka.com/muhammara/)
   - [chicken tikka masala](https://www.seriouseats.com/chicken-tikka-masala-for-the-grill-recipe)
   - [chicken katsu curry](https://japan.recipetineats.com/katsu-curry-japanese-curry-rice-with-chicken-cutlet/)
   - [teriyaki salmon](https://www.justonecookbook.com/teriyaki-salmon-recipe/)
   - [Steamed Fish with Ginger and Scallions](https://food52.com/recipes/54441-steamed-fish-with-ginger-scallions)
   - [Baked Cod with Tomatoes and Olives](https://www.realsimple.com/food-recipes/browse-all-recipes/baked-cod-with-tomatoes-olives-and-capers)
   - [Spaghetti Carbonara](https://www.bbcgoodfood.com/recipes/ultimate-spaghetti-carbonara-recipe)
   - [Mac and Cheese](https://www.allrecipes.com/recipe/238691/simple-macaroni-and-cheese/)
   - [Penne alla Vodka](https://natashaskitchen.com/penne-alla-vodka-recipe/)
   - [fried rice](https://teafinbox.com/shop/home/rice/egg-fried-rice/)
   - [briyani](https://nomadette.com/one-pan-chicken-briyani/)
   - [california roll](https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQixv0pTny1WELDRhlX2IAbIpmeEVhZsTCRPfs8mhFWqhX7pck3)
   - [cheeseburger](https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQyToDAkYP33mayq-BjZ7o9eYgWDLQbsoSvIAYY8dtInhlu4-aI)
   - [chicken gyros](https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcTruR78-vIXf3mbroCnfAQSn4i4eDoqjxXv0TzsXMlV8bZSlXM2)
   - [chicken tacos](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRb2GP4B5RT_x2Px0DjTxJowVsf5IHpdRdMrFm5HQRbIlyD-lDD)
   - [crepes](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNYSgyVIh3p9mBlWsTIghMlsgqyI_VxmlxXhMDIii4F2XrRGPu)
   - [hummus](https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcRxaQ3q9yD7owPTTgchxsVpytJUKoqm8Q6fbe37X-IuINq7UQiG)
   - [nicoise salad](https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQc3KjD3ZDI84rRpEnKf27lLUEMmBU_bj5FpF2w8UsAwdXYuGAU)
   - [paella](https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcT2yNBDT6Ja4jh_jOMS03t2oehUbeQUuW6UeoZSE1hZnw54ljfb)
   - [spaghetti](https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcS3zztvCDn9-zO9DBnFkp7kt3wg8RZM-Vx20mE3Ti5Szfkbj4SB)
   - [thai peanut chicken salad](https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcS3zztvCDn9-zO9DBnFkp7kt3wg8RZM-Vx20mE3Ti5Szfkbj4SB)

     
### Credits:
- PasswordStrengthMeter by gustavaa under Apache-2.0 license
