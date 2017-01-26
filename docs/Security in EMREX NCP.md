# Security in EMREX NCP

User logs in using WAYF using userid and password.

CPR number from WAYF user is used to call STADS server requesting for student exam results.

When requesting student results from a university STADS server, the request is containing the CPR number is encrypted using SSL. When the student results are returned from the server, the result is encrypted using SSL. To ensure that the university server is what it claims to be, the SSL certificate is validated against the hostname of the university STADS server.

You can only request student results in NCP from universities registered with the NCP.

An XML document is compiled in NCP to be returned to the calling system. The document consists of name and birth date together with exam results. To secure that this data originates from the danish NCP and has not been tampered with, the data includes a digital signature of the document and it's contents.

The signed document is then zip-compressed and base64 encoded before being placed in the browser session locally on the users PC or other equipment. Transferral of the document from the NCP server to the browser session is encrypted using SSL.

The compression and base64 encoding makes the document unreadable to the human eye. 

From the browser session the document is transported encrypted using SSL to the SMP server where it is base63 decoded and decompressed back to readable form.

The CPR number is only used in the encrypted call to the university STADS server and does not otherwize leave the NCP server.