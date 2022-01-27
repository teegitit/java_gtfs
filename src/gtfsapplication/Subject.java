/*
 * Course: SE 2030
 * Fall 2020
 * GTFS Application
 * Name: Kenneth McDonough
 * Created: 10/11/2020
 * MIT License
 *
 * Copyright (c) 2020 Team C (Kenneth McDonough, William Lauer, Thy Lee, Luke Miller)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package gtfsapplication;

import java.util.List;

/**
 * Subject interface
 *
 * @author Kenneth McDonough
 * @version 10/11/2020
 */
public interface Subject {
    /**
     * Adds an observer to the subject
     * @param observer observer to add
     */
    void addObserver(Observer observer);

    /**
     * Removes an observer from the subject
     * @param observer observer to remove
     */
    void deleteObserver(Observer observer);

    /**
     * Notifies all added observers that a change occurred
     * @param objects A list of String, Stop, Trip, StopTime or Route objects;
     *                all other types ignored
     */
    void notifyObservers(List<Object> objects);
}
