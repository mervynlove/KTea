

@file:Suppress("NOTHING_TO_INLINE")


import android.annotation.SuppressLint
import android.graphics.*

/**
 * Create by MengWei at 2018/7/13
 */

inline operator fun Rect.component1() = this.left

inline operator fun Rect.component2() = this.top

inline operator fun Rect.component3() = this.right

inline operator fun Rect.component4() = this.bottom

inline operator fun RectF.component1() = this.left

inline operator fun RectF.component2() = this.top

inline operator fun RectF.component3() = this.right

inline operator fun RectF.component4() = this.bottom

inline operator fun Rect.plus(r: Rect): Rect {
    return Rect(this).apply {
        union(r)
    }
}

inline operator fun RectF.plus(r: RectF): RectF {
    return RectF(this).apply {
        union(r)
    }
}

inline operator fun Rect.plus(xy: Int): Rect {
    return Rect(this).apply {
        offset(xy, xy)
    }
}

inline operator fun RectF.plus(xy: Float): RectF {
    return RectF(this).apply {
        offset(xy, xy)
    }
}

inline operator fun Rect.plus(xy: Point): Rect {
    return Rect(this).apply {
        offset(xy.x, xy.y)
    }
}

inline operator fun RectF.plus(xy: PointF): RectF {
    return RectF(this).apply {
        offset(xy.x, xy.y)
    }
}

inline operator fun Rect.minus(r: Rect): Region {
    return Region(this).apply {
        op(r, Region.Op.DIFFERENCE)
    }
}

inline operator fun RectF.minus(r: RectF): Region {
    return Region(this.toRect()).apply {
        op(r.toRect(), Region.Op.DIFFERENCE)
    }
}

inline operator fun Rect.minus(xy: Int): Rect {
    return Rect(this).apply {
        offset(-xy, -xy)
    }
}

inline operator fun RectF.minus(xy: Float): RectF {
    return RectF(this).apply {
        offset(-xy, -xy)
    }
}

inline operator fun Rect.minus(xy: Point): Rect {
    return Rect(this).apply {
        offset(-xy.x, -xy.y)
    }
}

inline operator fun RectF.minus(xy: PointF): RectF {
    return RectF(this).apply {
        offset(-xy.x, -xy.y)
    }
}

inline infix fun Rect.and(r: Rect) = this + r

inline infix fun RectF.and(r: RectF) = this + r

@SuppressLint("CheckResult")
inline infix fun Rect.or(r: Rect): Rect {
    return Rect(this).apply {
        intersect(r)
    }
}

@SuppressLint("CheckResult")
inline infix fun RectF.or(r: RectF): RectF {
    return RectF(this).apply {
        intersect(r)
    }
}

inline infix fun Rect.xor(r: Rect): Region {
    return Region(this).apply {
        op(r, Region.Op.XOR)
    }
}

inline infix fun RectF.xor(r: RectF): Region {
    return Region(this.toRect()).apply {
        op(r.toRect(), Region.Op.XOR)
    }
}

inline operator fun Rect.contains(p: Point) = contains(p.x, p.y)

inline operator fun RectF.contains(p: PointF) = contains(p.x, p.y)

inline fun Rect.toRectF(): RectF = RectF(this)

inline fun RectF.toRect(): Rect {
    val r = Rect()
    roundOut(r)
    return r
}

inline fun Rect.toRegion() = Region(this)

inline fun RectF.toRegion() = Region(this.toRect())

inline fun RectF.transform(m: Matrix) = apply { m.mapRect(this@transform) }
