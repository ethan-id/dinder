## How to deploy the .jar to the remote server

Pull the latest code:
```
git checkout main
git pull origin main
```

Copy the .jar file onto the remote server:
```
cd Backend/target/
scp onetoone-1.0.0.jar <YourNetID>@coms-309-055.class.las.iastate.edu:
```

Use Your NetID password if prompted to provide a password.

SSH into the remote server:
```
ssh <YourNetID>@coms-309-055.class.las.iastate.edu
```

If you do `ls` your .jar should be listed in this directory.

Move the .jar and run it:
```
sudo mv onetoone-1.0.0.jar ../
cd ../
java -jar onetoone-1.0.0.jar
```
Type `screen` after the Springboot server starts. Do not press ctrl+c or anything else. Then hit enter.

Now the .jar should be running on the remote server! :)

If port 8080 is already in use run the following:
```
sudo fuser -k 8080/tcp
```

If the pipeline fails you need to restart the daemon process run the following commands
```
sudo systemctl daemon-reload
sudo systemctl restart docker
```
