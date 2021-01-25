# A-Simple-FTP-Client

## 写在前面
不像磁盘文件模拟系统，这次所有的方法只需要处理相对路径（准确的说，应该只是一个文件名或者文件夹名）。当需要下载、上传、重命名、删除时，总需要先用CD方法（唯一支持绝对路径的方法）转到对应的目录下，然后再直接使用相对路径从当前目录里找到所需要处理的文件或者文件夹。

例如，上传方法的参数只有本地文件的路径，我们不需要关心用户要把文件上传到哪里。因为用户已经通过点击GUI的方式调用了CD命令切换了当前目录，我们只需要直接把本地文件上传到当前目录就可以了。以上传为例：

```java
//不需要try-catch，把异常抛出交给调用者（GUI）去处理
upload(String localPath) throws Expection{
	String name;//把localPath的最后一节拿出来
	FileInputStream fis;//接入到本地文件
	ftpClient.storeFile(name,fis)//直接上传
}
```

1. login ☑

	登录功能.
1. cd ☑

	转到指定文件夹下.
1. md

	创建一个文件夹.
1. download ☑

	下载当前目录的指定文件到本地.
1. upload

	上传本地文件到当前目录.
1. delete

	删除文件或者文件夹.
1. rename

	重命名文件或者文件夹.

1. logout

	下线.
