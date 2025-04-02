# How to run:
**Note:** Use whatever Dockerfile you need to test

Takes a while as build is not showint the output
```shell
docker run -it --rm $(docker build -f Dockerfile-python-311 -q .)
```
Alternatively, you can first build the image and then run it:
```shell
docker build -f Dockerfile-python-311 -t ygg-java-sdk .
docker run -it --rm ygg-java-sdk
```

