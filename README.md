# quarkus-demo-api

* Local Tools Needed
  * https://quarkus.io/guides/cli-tooling
  * https://openjdk.org
  * https://podman-desktop.io or https://podman.io/docs/installation

## URLs
* Container Repo: https://quay.io/repository/openshift-tigerteam/quarkus-demo-api

# Commands 

To run: 
```shell
quarkus dev --clean
```

To test: 
```shell
quarkus test
```

To build the image locally: 
```shell
quarkus build --clean
podman build -f Containerfile -t quay.io/openshift-tigerteam/quarkus-demo-api:latest .
```

To run the image locally: 
```shell
podman network create quarkus-demo
podman run --name postgres \
  --network quarkus-demo \
  -e POSTGRES_USER=username \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  -d docker.io/library/postgres:latest
podman run --name quarkus-demo-api \
  --network quarkus-demo \
  -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://postgres:5432/postgres \
  -e QUARKUS_DATASOURCE_USERNAME=username \
  -e QUARKUS_DATASOURCE_PASSWORD=password \
  -p 8080:8080 \
  -d quay.io/openshift-tigerteam/quarkus-demo-api:latest
```

To push the image:
```shell
podman push quay.io/openshift-tigerteam/quarkus-demo-api:latest
```

You may need to login at quay.io `podman login`

# Deploy

This manifest contains everything: 
* namespace: quarkus-demo
* postgres database: deployment, service
* quarkus-demo-api: deployment, service and route

Log into the openshift cluster and run: 
```shell
oc apply deploy/manifests/deploy.yml
```

