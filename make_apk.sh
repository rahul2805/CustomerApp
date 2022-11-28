#!/bin/bash
mainPath=/home/neel/AndroidStudioProjects/TemplateCustomerApp

#JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/
#export JAVA_HOME

#PATH=$PATH:$JAVA_HOME
#export PATH

#ANDROID_HOME=/usr/lib/android-sdk/
#export ANDROID_HOME

#PATH=$PATH:$ANDROID_HOME
#export PATH

#PATH=$PATH:$ANDROID_HOME/cmdline-tools/tools/bin
#export PATH

p=$mainPath/app/src/main/assets/meta_data.txt
echo $1 > $p

appName='2s|.*|<string name="app_name">'$2'</string>|'

sed -i "$appName" $mainPath/app/src/main/res/values/strings.xml

id='7s/.*/        applicationId "com.abc.'$2'"/'
sed -i "$id" $mainPath/app/build.gradle

colorPrimary='3s|.*|<color name="colorPrimary">'$3'</color>|'
colorDark='4s|.*|<color name="colorPrimaryDark">'$4'</color>|'
colorAccent='5s|.*|<color name="colorAccent">'$5'</color>|'

sed -i "$colorPrimary" $mainPath/app/src/main/res/values/colors.xml
sed -i "$colorDark" $mainPath/app/src/main/res/values/colors.xml
sed -i "$colorAccent" $mainPath/app/src/main/res/values/colors.xml

cd $mainPath
./gradlew assembleDebug
cd -

mv $mainPath/app/build/outputs/apk/debug/app-debug.apk $mainPath/app/build/outputs/apk/debug/$2.apk
