# Important Notice
Please be aware that I'm struggling to maintain this plugin.  Even after considerable effort, I have been unable to produce a reliable development environment.

While I will work to rectify this, neither Android or Cordova/ Phonegap development represent a priority to me.  Neither present me with paid income or particularly excite me enough to put aside other projects.

I will make attempts to be more active in responding to questions – and apologies for any previous delays.

Please keep the above in mind before actively using this plugin.

# Scheduler Plugin

## Quick summary
Plugin which allows you to schedule an alarm which will start your app.
 
## Getting started
The plugin can be installed using the following commands (this assumes you are familiar with the [Cordova Android Getting Started] (http://docs.phonegap.com/en/3.3.0/guide_platforms_android_index.md.html#Android%20Platform%20Guide)):

* cordova create hello com.example.hello "HelloWorld"
* cd hello
* cordova platform add android
* cordova plugin add https://github.com/Red-Folder/Scheduler-Plugin.git
* Amend the hello\www\config.xml, replace any existing content node with: `<content src="scheduler.html" />` (add the content node if not already present)
* cordova build
* Add the following to platforms\android\src\com\example\hello\HelloWorld.java in the onCreate() method:
```
// You will also need to add - import com.red_folder.phonegap.plugin.scheduler.ActivityHelper; at the top of the java file
ActivityHelper.onCreate(this);
```
* You should then be able to open, build the Android project (platforms\android) within Eclipse

You should be able to run the example.

Further documentation can be found at https://github.com/Red-Folder/Scheduler-Plugin/wiki

JavaScript reference can be found at https://github.com/Red-Folder/Scheduler-Plugin/wiki/Javascript-Method-Reference

## Development
This is a very early version of the plugin and is under active development.

Please see the [Enhancements List] (https://github.com/Red-Folder/Scheduler-Plugin/issues?labels=enhancement&page=1&state=open) for the planned changes. 

Please read the [Release Notes] (https://github.com/Red-Folder/Scheduler-Plugin/wiki/Release-Notes) between version as function signature is very likely to change while in active development.

## Questions and Support
If you have problems, then please log an issue: https://github.com/Red-Folder/Scheduler-Plugin/issues?state=open

As the plugin is updated (for new features or fixes) I will tweet via @FolderRed and blog http://red-folder.blogspot.co.uk/

## Spread the love

If you find the Plugin useful then spread the love.

All the work I do on the Plugin is done in my spare time - time I would otherwise be spending taking my wife out for a nice meal or helping my lad find vinyl records (he's currently very much into The Smiths, Fleetwood Mac and Kate Bush).

The Plugin is free and will always remain free. I will continue to develop, maintain and distribute the Plugin under the Apache 2.0 License.

https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=E64TCFQ3NLHZ8

## Licence
Copyright 2014 Red Folder Consultancy Ltd
    
Licensed under the Apache License, Version 2.0 (the "License");   
you may not use this file except in compliance with the License.   
You may obtain a copy of the License at       
  
http://www.apache.org/licenses/LICENSE-2.0   
 
Unless required by applicable law or agreed to in writing, software   
distributed under the License is distributed on an "AS IS" BASIS,   
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   
See the License for the specific language governing permissions and   
limitations under the License.
