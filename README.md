# idcard-maker
This repo creates a QR code to be printed on an ID card.
## Format
The QR code's text is in the format `<VERSION>:<CODE>`.
* `<VERSION>`<br>
  The version number, e.g. `1.2.3`
* `<CODE>`<br>
  The encrypted key in Base-64: `<ORGANIZATION KEY><HALF OF PERSONAL KEY>`