[TOC]

# This project is use for robotium-solo

# How to Use

just add code in module `build.gradle`

```gradle
dependencies {
    ...
    androidTestCompile 'com.jayway.android.robotium:robotium-solo:5.6.0'
}
```

copy files at [this path](app/src/androidTest/java)

```sh
.
└── mdl
    └── sinlov
        └── test
            └── android
                └── ui
                    └── fast
                        ├── FastTest.java
                        ├── MDLTestCont.java        // launch setting here
                        ├── MDLTestSet.java
                        ├── ThirdTestJob.java
                        └── app
                            └── Test_1_launch.java  // test case at here

7 directories, 5 files
```

** Must set this at `MDLTestCont`

```java
LAUNCH_ACTIVITY = ""
```

this place is setting launch activity must use `packageName.MainActivity`

## start base test case

```java
public class Test_1_launch extends MDLTestSet {
    public void test_base() throws Exception {
        setTestTimes(5);
        setAutoClose(true);
        FastTest f = new FastTest() {
            @Override
            public void task() {
                mySolo().sleep(1000);
            }
        };
        fastTest(f);
    }
}
```


# License

---

Copyright 2016 sinlovgm@gmail.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
