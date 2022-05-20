apiVersion: v1
kind: Pod
metadata:
  name: hostpath-pod
spec:
  containers:
  - name: test-container
    image: nginx
    volumeMounts:
    - mountPath: /test-nginx
      name: myhostpath
  volumes:
  - name: myhostpath
    hostPath:
      path: /tmp/nginx
      type: DirectoryOrCreate