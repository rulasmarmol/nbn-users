# nbn-users

### What you'll need

* [JDK 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
* [Docker](https://www.docker.com/products/docker-desktop)
* Kubernetes enabled
* [Apache Maven](https://maven.apache.org/download.cgi)
* If you already installed minikube, then you need to switch the context to docker-desktop (instead of minikube), otherwise the load balancer will not work. You can change the context in the context menu of docker or you can try this:
```
kubectl config use-context docker-for-desktop
```
	

## 1. Database setup

### Deployment config
```
kubectl apply -f mongo-deployment.yaml
```

### Service startup
```
kubectl apply -f mongo-service.yaml
```

### Verify installation
```
kubectl get all 
```
You should see something like this:
```
NAME                       TYPE           CLUSTER-IP      EXTERNAL-IP   PORT(S)        AGE
service/database           ClusterIP      None            <none>        <none>         4h5m

NAME                                  READY   AGE
statefulset.apps/mongodb-standalone   1/1     4h6m
```

## 2. Application setup

### Compile and download dependencies
```
mvn package
```

### Build and image creation
```
docker build -t nbn-user:latest .
```

### Deployment config
```
kubectl apply -f deployment.yaml
```

### Service startup
```
kubectl apply -f service.yaml
```

### Verify installation
```
kubectl get all 
```
You should see something like this:
```
NAME                                       READY   STATUS    RESTARTS   AGE
pod/nbn-user-deployment-5d5bfff8b4-bbmpk   1/1     Running   0          4h6m
pod/nbn-user-deployment-5d5bfff8b4-c8sqp   1/1     Running   0          4h6m
pod/nbn-user-deployment-5d5bfff8b4-ds4f8   1/1     Running   0          4h6m
pod/nbn-user-deployment-5d5bfff8b4-fhknk   1/1     Running   0          4h6m
pod/nbn-user-deployment-5d5bfff8b4-grl2l   1/1     Running   0          4h6m
pod/nbn-user-deployment-5d5bfff8b4-n9bx8   1/1     Running   0          4h6m
pod/nbn-user-deployment-5d5bfff8b4-px9sc   1/1     Running   0          4h6m
pod/nbn-user-deployment-5d5bfff8b4-q5fzd   1/1     Running   0          4h6m
pod/nbn-user-deployment-5d5bfff8b4-v2nbd   1/1     Running   0          4h6m
pod/nbn-user-deployment-5d5bfff8b4-w2pms   1/1     Running   0          4h6m

NAME                       TYPE           CLUSTER-IP      EXTERNAL-IP   PORT(S)        AGE
service/nbn-user-service   LoadBalancer   10.104.42.220   localhost     80:31253/TCP   4h6m

NAME                                  READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/nbn-user-deployment   10/10   10           10          4h6m

NAME                                             DESIRED   CURRENT   READY   AGE
replicaset.apps/nbn-user-deployment-5d5bfff8b4   10        10        10      4h6m
```
It make take some time to get all the pods running, so maybe you will see a Creating STATUS instead of Running, or something like that. 

At this point everything is running and you can test the application on your browser, but you don't have any id to test, so you need to retrieve some ids from the database, otherwise you're going to get an empty object. If you just want to if the application is running then go to step four and type anything on the id.

## 3. Fetch some ids from the database

### Connect to the container using sh
```
kubectl exec -it mongodb-standalone-0 sh
```

### Connect to the database 
```
mongo mongodb://database:27017
```

### Select dbs
```
use test
```

### Fetch some ids
```
db.user.find()
```
Then you will see something like this:
```
{ "_id" : ObjectId("5e86782bb56123011271f5a4"), "name" : "Carlos", "age" : 32, "_class" : "nbn.entities.User" }
{ "_id" : ObjectId("5e86782bb56123011271f5a5"), "name" : "Citlalli", "age" : 29, "_class" : "nbn.entities.User" }
```

## 4. Test 
Type this in the address bar of your browser:
```
http://localhost:80/user?id=5e86782bb56123011271f5a4
```
You will see one of the previous registers in the database:
```
{"id":"5e86782bb56123011271f5a4","name":"Carlos","age":32}
```

# Reference 

1. To build a spring boot application with rest: [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/).

2. To deploy your spring boot application in docker: [Spring Boot with Docker](https://spring.io/guides/gs/spring-boot-docker/).

3. To connect your spring boot application with mongodb: [Accessing Data with MongoDB](https://spring.io/guides/gs/accessing-data-mongodb/).

4. How to run mongodb on docker: [mongo](https://hub.docker.com/_/mongo).

5. Introduction to kubernetes (Spanish): [Introduccion a Kubernetes (Practica)](https://www.youtube.com/watch?v=6jeCUFNv0XI&t=12s). 

6. Create Kubernete service for mongodb: [Standalone Mongodb on Kubernetes Cluster](https://medium.com/@dilipkumar/standalone-mongodb-on-kubernetes-cluster-19e7b5896b27).

7. Problems with minikube: [kubernetes service external ip pending](https://stackoverflow.com/questions/44110876/kubernetes-service-external-ip-pending)

8. Problems pushing an image to docker repository: [Docker push - Error - requested access to the resource is denied](https://forums.docker.com/t/docker-push-error-requested-access-to-the-resource-is-denied/64468/2). 

9. Running queries on mongodb: [Basic Shell JavaScript Operations¶](https://docs.mongodb.com/manual/reference/mongo-shell/).
