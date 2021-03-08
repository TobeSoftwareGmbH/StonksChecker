<p align="center">
 <img width="10%" src="/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" align="center" alt="StonksChecker Logo" />
 <h1 align="center">StonksChecker</h1>
 <p align="center">An Android App that changes the meme (Stonks, Not Stonks, Confused Stonks) of a widget depending on the state of the tracked stock. Absolutely perfect for successful investors and those who want to be.</p>
</p>
  <p align="center">
    <a href="https://snyk.io/test/github/TobeSoftwareGmbH/StonksChecker?targetFile=app/build.gradle">
        <img src="https://snyk.io/test/github/TobeSoftwareGmbH/StonksChecker/badge.svg?targetFile=app/build.gradle" alt="Known Vulnerabilities" data-canonical-src="https://snyk.io/test/github/TobeSoftwareGmbH/StonksChecker?targetFile=app/build.gradle" style="max-width:100%;">
    </a>
    <a href="https://codeclimate.com/github/kammt/StonksChecker/maintainability">
      <img src="https://api.codeclimate.com/v1/badges/414152d683962a378382/maintainability" />
   </a>
    <a href="https://travis-ci.com/kammt/StonksChecker">
      <img src="https://travis-ci.com/kammt/StonksChecker.svg?branch=main" />
    </a>
  </p>
  
## Overview and Features
- **Track your Stocks** - Add widgets to your homescreen using a simple UI and let the *best meme template on the internet* show you the stock price history
- **Dark Mode** - Because that's what all the cool kids use, right?
- **Minimal permissions used.** The following permissions are required:
  - *INTERNET* - To be able to communicate with Bloomberg Markets
  - *RECEIVE_BOOT_COMPLETED* - To be able to reschedule all widget refreshes when the device reboots
- **Free and Open-Source** - Licensed under the GPLv3-license
  
## Screenshots
<img src="/project-screenshots/widget_homescreen.png" width="20%"> <img src="/project-screenshots/widget_configuration.png" width="20%"> <img src="/project-screenshots/widget_homescreen_multiple.png" width="20%"> 

## State of the App
This app is currently in alpha stage. While the main features are currently working, some work is still needed to make it fully functional. The following features are planned:
- Editing widgets after adding them
- Stonks/Not Stonks based on a provided Stock value (e.g. the value you bought the stock at)

## License and Attribution
- This project uses the following libraries:
  - <a href="https://github.com/google/volley">Volley</a>, licensed under the <a href="https://choosealicense.com/licenses/apache-2.0/">Apache-2.0 license</a>
- This App queries <a href="https://www.bloomberg.com/markets">Bloomberg Markets</a> for Financial Data. This is neither supported nor endorsed by Bloomberg Markets. This app comes with absolutely no warranty and you are responsible for the traffic that you generate while using this app.
- This project uses the following fonts:
  - Comfortaa, designed by Johan Aakerlund, Cyreal, licensed under the <a href="https://scripts.sil.org/cms/scripts/page.php?site_id=nrsi&id=OFL">Open Font license</a>
- This project uses some <a href="https://github.com/google/material-design-icons">Material Design Icons</a>, which are licensed under the <a href="https://choosealicense.com/licenses/apache-2.0/">Apache-2.0 license</a>
- This project is licensed as per the GPL-v3 license. See the <a href="LICENSE">license file</a> for more information
