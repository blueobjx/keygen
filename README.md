# keygen tool

Simple tool to generate an AES-256 key via Java's KeyGenerator class.  In addition to generating a key directly, you can also use it to merge a new key into a file.

## Usage

### Generate a key

```bash
keygen
```

### Merge a key in a file

To read a file and replace {{INSERT_GENERATED_MSGKEY}} entries with the key:

```bash
keygen filepath > outputfile
```

## Openssl

The only real purpose for this script is the merge aspect of it and if you want to be sure your getting a version like how Java would generate it.  With openssl, you can create AES-256 keys easily with openssl, like so:

```bash
openssl rand -base64 32
```
