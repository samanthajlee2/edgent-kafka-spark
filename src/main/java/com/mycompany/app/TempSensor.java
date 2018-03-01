/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/

package com.mycompany.app;
import java.util.Random;
import org.apache.edgent.function.Supplier;

/**
 * Every time get() is called, TempSensor generates a temperature reading.
 */
public class TempSensor implements Supplier<String> {
    public static final int HIGH = 100;
    public static final int LOW = 30;

    private static final long serialVersionUID = 1L;

    double currentTemp = 65.0;
    Random rand;

    TempSensor(){
        rand = new Random();
    }

    @Override
    public String get() {
        // Change the current temperature some random amount
        double newTemp = rand.nextGaussian() + currentTemp;
        currentTemp = newTemp;
        if (currentTemp > HIGH) {
            currentTemp = HIGH;
        } else if (currentTemp < LOW) {
            currentTemp = LOW;
        }
        return Double.toString(currentTemp);
    }
}
