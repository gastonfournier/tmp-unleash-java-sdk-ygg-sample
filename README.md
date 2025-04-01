# How to run:
Takes a while as build is not showint the output
```shell
docker run -it --rm $(docker build -q .)
```
Alternatively, you can first build the image and then run it:
```shell
docker build -t ygg-java-sdk .
docker run -it --rm ygg-java-sdk
```

