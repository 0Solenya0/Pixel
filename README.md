
# Social-Network  
  
## Resources:  
### Libraries:  
- Gson Library  
### Codes:  
- [Stackoverflow](https://stackoverflow.com/questions/39192945/serialize-java-8-localdate-as-yyyy-mm-dd-with-gson) Gson DateTime and LocalDateTime Adapter  

## Karkard:  
### Models:  
- Har model az abstract class e Model ers bari mikonad  
- Address zakhireye har model be soorate `./db/full_class_name/id.json` mibashad  
- Functionality save/delete/load dar model az class Model ers bari mishavad niaz be tanzimate digar nist  
- Hengame save kardan:
	- Agar $Id = 0$ model jadid boode va bayad zakhire shavad
	- Agar $Id\neq0$ model ba in Id update khahad shod
### Fields:
- RelType moshakhas konandeye vaziyate dostiye 2 karbar ast (Follow/Block)
- RelStatus neshan dahandeye vaziyate rabete beyne 2 karbar ast
- UserField yek type migirad va an ra ba yek AccessLevel pair mikonad estefade dar (mail/lastseen/phone/...)
### Model Filters:
- Baraye abstract ModelFilter az generic class ha estefade shod ke tavabe yeksan dar abstract class biayand
### Client:
- Har page be soorate yek taabe dar class marbote taarif shode, baraye back kafist dar taabe page return konim ta be menu ghabli beravim
- UserUtility class be onvane database estefade shode, hamchenin tavabeyi ke dar kole code ziad estefade mishavad ra ham shamel mishavad
<br />
<br />

### Positive Points:
- Sakht e Model kare ziadi nemibarad kafist ke az abstract class Model extend shavad
- Taabe validation ghabl az save etminan hasel mikonad ke etelaat na motabar dar database save nashavad
- customFilter dar ModelFilter emkane sakht filter haye az  mpish taarif nashode ra farahamikonad va mitavan az tavabe java dar an estefade kard
- Exception ha daste bandi shodand be in soorat baraye ejad taaghirat dar exception va message an kafist class e marbote taghir dade shavad

### Negative Points:
- Filter ha majboor be for zadan roye tamami id ha hastan
- barkhi tavabe static manand `get()` dar User ers bari nemishavand
