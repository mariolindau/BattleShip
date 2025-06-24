# README.md

# Ülesanded

1. Edetabelit ei saa vaadata mängu ajal.
2. Mängulaua suurust ei saa muuta mängu ajal. 
3. Mängu lõppedes ei kao lõppseis ekraanilt 
4. Kui muudetakse mängulaua suurust, siis eelmise mängu seis kustub (hetkel jookseb kokku.)
5. Kogu mängu aknal võiks olla miinimum suurus, millest väiksemaks ei saa.
6. Edetabeli linnukese eemaldamisel/puudumisel, tuleb põhiaknale edetabel kuvada. 
Teiste paneelide/frame peale. Ise pead valima kuidas. Peab olema koos sulgemisnupuga. (FlowLayout).


    **Kui on küsimusi, siis võta Teams Chat vahendusel ühendust**

## Tehtud muudatused:
- Rakendatud piirang, et mängu ajal ei saa vaadata edetabelit ega muuta laua suurust.
- Lõppseis jääb mängu lõppedes ekraanile kuni uue mängu alustamiseni.
- Eelmise mängu seis kustub õigesti, kui mängulaua suurust muudetakse.
- Aken ei ole enam skaleeritav alla määratud miinimumi (`850 x 550`).
- Edetabelit saab kuvada:
  - Eraldi aknas (`JDialog`) või
  - Sama akna sees (`JPanel`), kui linnuke on eemaldatud.
- Edetabeli sulgemine `JPanel` puhul toimub eraldi **"Sulge"** nupuga (`FlowLayout`).

## Täiendavad väiksemad muudatused

- Lisatud eraldi sulgemisnupp edetabeli `JPanel`-versiooni jaoks.
- Puhastatud mittevajalikud klassid ja import-read (refaktoreerimine).
- Sünkroniseeriti GUI ja loogika olekud, et vältida visuaalseid või loogilisi vigu.
- Pärast mängu lõppu ja nime sisestamist edetabelisse aktiveeritakse taas ruudustiku suuruse valik ning edetabeli nupp.
- Vaikimisi valikuks edetabeli laadimisel jäi failipõhine vaade.
- Mängu andmed salvestatakse täpse kuupäeva ja kellaajaga edetabelisse.
- Käivitusfailis (`App.java`) määrati akna asukoht keskele ja lubati sulgemine.