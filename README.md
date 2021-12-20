# idcard-maker
This repo creates a QR code to be printed on an ID card.
## Format
The QR code's text is in the format `<VERSION>:<CODE>`.
* `<VERSION>`<br>
  The version number, e.g. `1.2.3`
* `<CODE>`<br>
  The encrypted key in Base-64: `<ORGANIZATION KEY><HALF OF PERSONAL KEY>`

All organization keys should be 32 characters long, and personal keys 64.

### Example

|      **Employee**     |                          **Personal key**                          |
|:---------------------:|:------------------------------------------------------------------:|
| Norris Poker          | `8a46420915fe76fc96f7314df69a2bb9b1a39894a8731b09480ba8d9f9a83d5d` |
| Frederico McJeannason | `c30e20c17c42189d674e5f2565806410c51e0ea550680d4d763f99c9354f3435` |
| Grady Bolan           | `b076426a74a369699ab3c32e10d9e712788b3c8681448b11305bf98f8f4eb95c` |

Here is an example of how the QR codes on an employee ID card would be generated for a hypothetical employee Norris Poker with a personal key of  `8a46420915fe76fc96f7314df69a2bb9b1a39894a8731b09480ba8d9f9a83d5d`, working for a hypothetical organization with an organization key of `5b9faafa3918abe2a0f139fcfb80c3f4`.

1. For the front half of the card, combine the organization key (`5b9faafa3918abe2a0f139fcfb80c3f4`) with the first half of the personal key (`8a46420915fe76fc96f7314df69a2bb9`). For the back half of the card, use the second half of the key (`b1a39894a8731b09480ba8d9f9a83d5d`) instead of the first.<br><br>
   We end up with `5b9faafa3918abe2a0f139fcfb80c3f48a46420915fe76fc96f7314df69a2bb9` for the front and `5b9faafa3918abe2a0f139fcfb80c3f4b1a39894a8731b09480ba8d9f9a83d5d` for the back.
2. Now, encrypt the front and back hex strings with the SHA-256 algorithm. The algorithm should use the hex strings as representations of bytes, not as strings of text. The outputs should be in Base-64 using a charset of `A`-`Z`, `a`-`z`, `0`-`9`, `-`, and `_`, ending with an equals sign (`=`).<br><br>
  We end up with Base-64 strings for the front (`BMthw4dp4nQXklwUH_BgVlPVEd8pZsIasMmNfpLb0mY=`) and back (`OZ4B2YdTlRT3KsvXdrV1XrnMeDKjJFflBcn4-A2BFbQ=`).

## See also
* Useful for checking SHA-256 encrpytion: https://string-o-matic.com/sha256