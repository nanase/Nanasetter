/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Tomona Nanase
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.nanase.nanasetter.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Project: Nanasetter
 * Created by nanase on 14/07/21.
 */

/**
 * 重複を許さない、順序つき Twitter オブジェクトのリストを提供します。
 *
 * @author Tomona Nanase
 * @since Nanasetter 0.1
 */
public class TwitterList extends ArrayList<Twitter> {
    /**
     * Appends the specified element to the end of this list.
     *
     * @param twitter element to be appended to this list
     * @return <tt>true</tt> (as specified by {@link java.util.Collection#add})
     */
    @Override
    public boolean add(Twitter twitter) {
        return !this.contains(twitter) && super.add(twitter);
    }

    /**
     * Inserts the specified element at the specified position in this
     * list. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     *
     * @param index   index at which the specified element is to be inserted
     * @param element element to be inserted
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public void add(int index, Twitter element) {
        if (!this.contains(element))
            super.add(index, element);
    }

    /**
     * Appends all of the elements in the specified collection to the end of
     * this list, in the order that they are returned by the
     * specified collection's Iterator.  The behavior of this operation is
     * undefined if the specified collection is modified while the operation
     * is in progress.  (This implies that the behavior of this call is
     * undefined if the specified collection is this list, and this
     * list is nonempty.)
     *
     * @param c collection containing elements to be added to this list
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean addAll(Collection<? extends Twitter> c) {
        return !c.stream().anyMatch(this::contains) && super.addAll(c);
    }

    /**
     * Inserts all of the elements in the specified collection into this
     * list, starting at the specified position.  Shifts the element
     * currently at that position (if any) and any subsequent elements to
     * the right (increases their indices).  The new elements will appear
     * in the list in the order that they are returned by the
     * specified collection's iterator.
     *
     * @param index index at which to insert the first element from the
     *              specified collection
     * @param c     collection containing elements to be added to this list
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws NullPointerException      if the specified collection is null
     */
    @Override
    public boolean addAll(int index, Collection<? extends Twitter> c) {
        return !c.stream().anyMatch(this::contains) && super.addAll(index, c);
    }

    /**
     * リスト中から該当する Id を持つ Twitter オブジェクトを取得します。
     *
     * @param id Twitter の Id。
     * @return Twitter オブジェクトを内包する {@code Optional<Twitter>} オブジェクト。
     */
    public Optional<Twitter> getTwitter(long id) {
        for (Twitter t : this) {
            try {
                if (t.getId() == id)
                    return Optional.of(t);
            } catch (TwitterException e) {
                //
            }
        }

        return Optional.empty();
    }
}
