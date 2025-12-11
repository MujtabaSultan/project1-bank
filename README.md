# user stories:
1 - As a user, I want to deposit, withdraw, and transfer money to my own or other users’ accounts so that I can manage my finances easily.

2 - As a user, I want my account to be locked after multiple failed login attempts so that my account is protected from fraudulent access.

3 - As a user, I want to view my transaction history so that I can track my spending and account activity.

4 - As a user, I want to view my account details, including my balance and personal information, so that I can monitor my finances and verify my information.

5 - As a user, I want to create an account and log in so that I can securely access my bank services.

# used technologoies:
1 - java

2 - junit

3 - jbcrypt 

4 - maven

# unresolved issues :
1 - the current implementation for banker doesnt make him unique , also no way to create banker using cli , the only way is to change the Banker to true in the file , and that only gives him a different menu thats says banker and thats it 

2 - the current overdraft implementation doesnt increments by 1 on wrong or overdraft worthy operation , yet after becoming positive it doesnt reset the count overdraft attempts , i think it should stay this way.

3 - there is no direct way for the user to add other accounts or change the cards 

# favourite part :
1 - the load customer by email really did me a huge favour specially since there was no usage of data base in the project so it facilitated operations to foreign users 

# ERD :

![ER Diagram](Screenshot 2025-12-11 at 4.26.48 PM.png)


