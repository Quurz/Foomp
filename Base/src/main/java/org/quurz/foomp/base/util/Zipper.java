
package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Tuple2.tuple2;

/**
 * <div>
 *   <p>
 *     Utility class providing operations to combine and separate parallel lists.
 *   </p>
 *   <p>
 *     Semantics:
 *     <ul>
 *       <li><b>Zip</b>: combines two lists element-wise into a list of tuples.</li>
 *       <li><b>Unzip</b>: separates a list of tuples into two parallel lists.</li>
 *       <li><b>Length handling</b>: {@code zip} truncates to the shorter list length.</li>
 *     </ul>
 *   </p>
 *   <p>
 *     Contract: all inputs must not be {@code null}. List elements must not be {@code null}.
 *   </p>
 *   <p>
 *     Examples:
 *   </p>
 *   <pre>{@code
 *   var numbers = List.of(1, 2, 3);
 *   var letters = List.of("a", "b", "c", "d");
 *
 *   var zipped = Zipper.zip(numbers, letters);
 *   // Result: [(1, "a"), (2, "b"), (3, "c")]
 *   // Note: "d" is ignored as numbers list is shorter
 *
 *   var unzipped = Zipper.unzip(zipped);
 *   // Result: ([1, 2, 3], ["a", "b", "c"])
 *   }</pre>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class Zipper {

    /**
     * <div>
     *   <p>
     *     Combines two lists element-wise into a list of {@link Tuple2} pairs.
     *   </p>
     *   <p>
     *     The resulting list has the length of the shorter input list.
     *     Elements beyond the shorter list's length are ignored.
     *   </p>
     *   <p>
     *     Example:
     *   </p>
     *   <pre>{@code
     *   var as = List.of(1, 2, 3);
     *   var bs = List.of("x", "y");
     *   var result = Zipper.zip(as, bs);
     *   // Result: [(1, "x"), (2, "y")]
     *   }</pre>
     * </div>
     *
     * @param as   the first list; must not be {@code null}
     * @param bs   the second list; must not be {@code null}
     * @param <A>  type of elements in the first list
     * @param <B>  type of elements in the second list
     * @return a list of {@link Tuple2} containing paired elements; never {@code null}
     * @throws NullPointerException if {@code as} or {@code bs} is {@code null}
     *
     * @since 1.0.0
     */
    public static <A, B> List<Tuple2<A, B>> zip(final @NonNull List<A> as,
                                                final @NonNull List<B> bs) {
        Objects.requireNonNull(as, nullValue("as"));
        Objects.requireNonNull(bs, nullValue("bs"));

        final int size
            = Math.min(as.size(), bs.size());
        final var result
            = new ArrayList<Tuple2<A, B>>();
        for (int i = 0; i < size; i++) {
            result.add(tuple2(as.get(i), bs.get(i)));
        }
        return result;
    }

    /**
     * <div>
     *   <p>
     *     Separates a list of {@link Tuple2} pairs into two parallel lists.
     *   </p>
     *   <p>
     *     The first list contains all first components, the second list contains all second components.
     *     The order of elements is preserved.
     *   </p>
     *   <p>
     *     This operation is the inverse of {@link #zip(List, List)}.
     *   </p>
     *   <p>
     *     Example:
     *   </p>
     *   <pre>{@code
     *   var tuples = List.of(
     *       Tuple2.tuple2(1, "x"),
     *       Tuple2.tuple2(2, "y"),
     *       Tuple2.tuple2(3, "z")
     *   );
     *   var result = Zipper.unzip(tuples);
     *   // Result: ([1, 2, 3], ["x", "y", "z"])
     *   }</pre>
     * </div>
     *
     * @param tuples the list of tuples to separate; must not be {@code null}
     * @param <A>    type of the first component
     * @param <B>    type of the second component
     * @return a {@link Tuple2} containing two lists with separated components; never {@code null}
     * @throws NullPointerException if {@code tuples} is {@code null}
     *
     * @since 1.0.0
     */
    public static <A, B> Tuple2<List<A>, List<B>> unzip(final @NonNull List<Tuple2<A, B>> tuples) {
        Objects.requireNonNull(tuples, nullValue("tuples"));

        final var as
            = new ArrayList<A>();
        final var bs
            = new ArrayList<B>();

        for (final var tuple : tuples) {
            as.add(tuple.get1());
            bs.add(tuple.get2());
        }

        return tuple2(as, bs);
    }

    /**
     * <div>
     *   <p>
     *     Private constructor to prevent instantiation of this utility class.
     *   </p>
     * </div>
     *
     * @since 1.0.0
     */
    private Zipper() {}

}