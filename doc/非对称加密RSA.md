### 非对称加密

非对称加密算法有：RSA,DSA,ECC,DH.
其中RSA最为常用.

非对称加密一般有一对公钥和私钥, 因为加解密使用不同密钥，所以称为非对称加密.

非对称加密的原理是基于一个数论：两个大素数的乘积很容易计算，而乘积结果很难分解[^1].
基于这个原理，可以将乘积公开作为加密密钥，即公钥，而两个大素数组合成私钥。[^2]
RSA的密钥生成算法如下:

```
随意选择两个大的质数p和q，p不等于q，计算N=pq。
根据欧拉函数，不大于N且与N互质的整数个数为(p-1)(q-1)
选择一个整数e与(p-1)(q-1)互质，并且e小于(p-1)(q-1)
用以下这个公式计算d：d × e ≡ 1 (mod (p-1)(q-1))
将p和q的记录销毁。
(N,e)是公钥，(N,d)是私钥。
```

非对称加密有如下特点：

* 如果使用公钥加密，那么只能使用私钥解密
* 如果使用私钥解密，那么只能使用公钥加密

由于公钥是公开的，因此，公钥加密的使用场景一般是登陆场景，比如ssh配置`authorized_keys`自动登陆

非对称加密保密性好，消除了明文交换密钥的过程，但是加解密过程远远慢于对称加密.因此一般常见的做法是使用非对称加密传递对称加密密钥.

常见的使用场景如下：

1. 信息加密：
   收信者是唯一能解开加密信息的人.这种情况，收信人必须持有私钥.发信人持有公钥，因为公钥公开，因此其他人可以伪造发信.

2. 登陆认证：
   客户端发送认证标识给服务器，服务器是唯一能正确解密标识的人.这种情况下，客户端持有私钥，服务器持有公钥.

3. 数字签名：
   数字签名是为了表明信息没有被伪造，确认是信息所有者发出来的，附在信息原文后边，对文件进行哈希运算，然后使用私钥加密，任何持有公钥的人即可确认这段文件是私钥持有者发送.

4. 数字证书
   获取公钥的时候如何确认公钥是真的，没有被中间人篡改呢？这就需要第三方公证机构来确保公钥合法可信，这种三方机构被称之为CA(
   Certificate Authority).
   CA使用自己的私钥对信息原文所有者的公钥和相关信息进行加密，得到的内容就称之为数字证书.可靠CA的公钥一般内置在操作系统或浏览器中.

#### java中加解密

在java中加解密使用到如下代码:

```
import javax.crypto.Cipher;
……
    Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE,publicKey);
    return cipher.doFinal(message);
```

##### Cipher初始化transformation介绍[^3]

cipher的生成参数是transformation(转换模式).
转换模式一般由三个部分构成，格式是：算法/工作模式/填充模式(algorithm/mode/padding),

例如：RSA/ECB/OAEPWithMD5AndMGF1Padding.

这里transformation结构中，算法为必输项，工作模式默认ECB,填充模式默认是PKCS5padding

SunJCE Provider支持的Cipher的部分详细信息如下：

|algorithm(算法)| 	mode(工作模式) | 	padding(填充模式)  |
|----|-----------------------------------|-----------------------------------------|
|AES| 	EBC、CBC、PCBC、CTR、CTS、CFB、CFB8-CFB128等 | 	NoPadding、ISO10126Padding、PKCS5Padding |
|AESWrap| 	EBC                              | 	NoPadding                              |
|ARCFOUR| 	EBC                              | 	NoPadding                              |
|Blowfish、DES、DESede、RC2| 	EBC、CBC、PCBC、CTR、CTS、CFB、CFB8-CFB128等 | 	NoPadding、ISO10126Padding、PKCS5Padding| 
|DESedeWrap| 	CBC      | 	NoPadding|
|PBEWithMD5AndDES、PBEWithMD5AndTripleDES、PBEWithSHA1AndDESede、PBEWithSHA1AndRC2_40| 	CBC| 	PKCS5Padding|
|RSA| 	ECB、NONE                         | 	NoPadding、PKCS1Padding等                |

###### 1. 算法

算法就是据具体加解的算法名称，例如：`SHA-256,RSA`;

###### 2. 工作模式

工作模式主要针对分组密码，分组密码是将明文消息编码表示后的数字序列划分成长度为n的组，每组分别在密钥的控制下变换成登场的密文数字.
工作模式主要是为了适应：

* 需要加密的明文长度十分巨大，为了性能，需要分组加密
* 多次使用相同的密钥对不同分组加密可能会导致安全问题

NIST定义了五种工作模式：

|模式| 	名称                                                                         | 	描述                                                         | 	典型应用                |
|---|-----------------------------------------------------------------------------|-------------------------------------------------------------|----------------------|
|电子密码本(ECB)	| Electronic CodeBook| 	用相同的密钥分别对明文分组独立加密| 	单个数据的安全传输(例如一个加密密钥) |
|密码分组链接(CBC)| 	Cipher Block Chaining| 	加密算法的输入是上一个密文组合下一个明文组的异或| 	面向分组的通用传输或者认证       |
|密文反馈(CFB)	| Cipher FeedBack| 	一次处理s位，上一块密文作为加密算法的输入，产生的伪随机数输出与明文异或作为下一单元的密文| 	面向分组的通用传输或者认证       |
|输出反馈(OFB)	| Output FeedBack| 	与CFB类似，只是加密算法的输入是上一次加密的输出，并且使用整个分组| 	噪声信道上的数据流的传输(如卫星通信) |
|计数器(CTR)	| Counter| 	每个明文分组都与一个经过加密的计数器相异或。对每个后续分组计数器递增| 面向分组的通用传输或者用于高速需求    |

###### 3.填充模式

块加密算法要求原文数据长度为固定块大小的整数倍，如果不足的部分需要填充，填充模式决定如何填充块.

Java原生支持的Padding(Cipher)汇总如下：

|填充模式| 描述                                                                                   |
|---|--------------------------------------------------------------------------------------------|
|NoPadding        | 不采用填充模式                                                                |
|ISO10126Padding  | XML加密语法和处理文档中有详细描述                                               |
|OAEPPadding, OAEPWith<digest>And<mgf>Padding| PKCS1中定义的最优非对称加密填充方案，digest代表消息摘要类型，mgf代表掩码生成函数，例：OAEPWithMD5AndMGF1Padding或者OAEPWithSHA-512AndMGF1Padding |
|PKCS1Padding     | 	PKCS1，RSA算法使用                                                           |                
|PKCS5Padding     | 	PKCS5，RSA算法使用                                                           |               
|SSL3Padding      | 	见SSL Protocol Version 3.0的定义                                             | 

[^1]:针对RSA最流行的攻击一般是基于大数因数分解。1999年，RSA-155（512 bits）被成功分解，花了五个月时间（约8000 MIPS年）和224 CPU
hours在一台有3.2G中央内存的Cray C916计算机上完成。
2009年12月12日，编号为RSA-768（768 bits, 232 digits）数也被成功分解。这一事件威胁了现通行的1024-bit密钥的安全性，普遍认为用户应尽快升级到2048-bit或以上。
[^2]:实际算法更为复杂，这里只是讲基础原理
[^3]:参考 https://www.cnblogs.com/throwable/p/9480540.html
