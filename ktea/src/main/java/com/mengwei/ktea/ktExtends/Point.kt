

@file:Suppress("NOTHING_TO_INLINE")


import android.graphics.Point
import android.graphics.PointF

/**
 * Create by MengWei at 2018/7/13
 */

inline operator fun Point.component1() = this.x

inline operator fun Point.component2() = this.y

inline operator fun PointF.component1() = this.x

inline operator fun PointF.component2() = this.y

inline operator fun Point.plus(p: Point): Point {
    return Point(x, y).apply {
        offset(p.x, p.y)
    }
}

inline operator fun PointF.plus(p: PointF): PointF {
    return PointF(x, y).apply {
        offset(p.x, p.y)
    }
}

inline operator fun Point.plus(xy: Int): Point {
    return Point(x, y).apply {
        offset(xy, xy)
    }
}

inline operator fun PointF.plus(xy: Float): PointF {
    return PointF(x, y).apply {
        offset(xy, xy)
    }
}

inline operator fun Point.minus(p: Point): Point {
    return Point(x, y).apply {
        offset(-p.x, -p.y)
    }
}

inline operator fun PointF.minus(p: PointF): PointF {
    return PointF(x, y).apply {
        offset(-p.x, -p.y)
    }
}

inline operator fun Point.minus(xy: Int): Point {
    return Point(x, y).apply {
        offset(-xy, -xy)
    }
}

inline operator fun PointF.minus(xy: Float): PointF {
    return PointF(x, y).apply {
        offset(-xy, -xy)
    }
}

inline operator fun Point.unaryMinus() = Point(-x, -y)

inline operator fun PointF.unaryMinus() = PointF(-x, -y)

inline fun Point.toPointF() = PointF(this)

inline fun PointF.toPoint() = Point(x.toInt(), y.toInt())
