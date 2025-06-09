# Holiday : Feature Documentation
- API
  - POST &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; /api/holiday POST<br>&nbsp;&nbsp;&nbsp;&nbsp;Register new Holiday to the DB
  - PUT &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; /api/holiday/{id:Long}<br>&nbsp;&nbsp;&nbsp;&nbsp;Update existing Holiday Record found by ID
  - DELETE &nbsp;&nbsp;&nbsp;&nbsp; /api/holiday/{id:Long}<br>&nbsp;&nbsp;&nbsp;&nbsp;Delete Holiday Record found by ID
  - GET &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; /api/holiday/{period:String}/{number:String}<br>&nbsp;&nbsp;&nbsp;&nbsp;Get Holiday Records matching requested Day or Year