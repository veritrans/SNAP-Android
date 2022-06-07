# Snap SDK android

## Table of contents

* [Support](#support)
* [Release variants](#release-variants)
    * [Download](#download)
        * [Current](#current)
        * [API documentation](#api-documentation)
    * [Verifying binaries](#verifying-binaries)
* [Building Snap SDK](#building-Snap-SDK)
* [Building Snap SDK](#building-Snap-SDK)
* [Security](#security)
* [License](#license)

## Support

Looking for help? Check out the
[instructions for getting support](SUPPORT.md).

## Release variants

* **CoreKit**:
* **UiKit**:

### Download

Binaries, arr, and source are available at
<https://source/>.


#### API documentation

Documentation for the latest Current release is at <https://doc.midtrans.com/api/>.
Version-specific documentation is available in each release directory in the
_docs_ subdirectory. Version-specific documentation is also at
<https://doc.midtrans.com/download/docs/>.

### Verifying binaries

Download directories contain a `SHASUMS256.txt` file with SHA checksums for the
files.

To download `SHASUMS256.txt` using `curl`:

```console
$ curl -O https://midtrans.com/snap/dist/vx.y.z/SHASUMS256.txt
```

To check that a downloaded file matches the checksum, run
it through `sha256sum` with a command such as:

```console
$ grep snap-vx.y.z.tar.gz SHASUMS256.txt | sha256sum -c -
```


## Building Snap

See [BUILDING.md](BUILDING.md) for instructions on how to build Snap from
source.

## Security

For information on reporting security vulnerabilities in Snap, see
[SECURITY.md](./SECURITY.md).


[Midtrans website]: https://midtrans.com/
