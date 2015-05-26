package com.pp.a10dance.view;//

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;

public class ImageViewBorder extends ShapeDrawable {
    private final Paint textPaint;
    private final Paint borderPaint;
    private final String text;
    private final int color;
    private final RectShape shape;
    private final int height;
    private final int width;
    private final int fontSize;
    private final float radius;
    private final int borderThickness;

    private ImageViewBorder(ImageViewBorder.Builder builder) {
        super(builder.shape);
        this.shape = builder.shape;
        this.height = builder.height;
        this.width = builder.width;
        this.radius = builder.radius;
        this.text = builder.toUpperCase ? builder.text.toUpperCase()
                : builder.text;
        this.color = builder.color;
        this.fontSize = builder.fontSize;
        this.textPaint = new Paint();
        this.textPaint.setColor(builder.textColor);
        this.textPaint.setAntiAlias(true);
        this.textPaint.setFakeBoldText(builder.isBold);
        this.textPaint.setStyle(Style.FILL);
        this.textPaint.setTypeface(builder.font);
        this.textPaint.setTextAlign(Align.CENTER);
        this.textPaint.setStrokeWidth((float) builder.borderThickness);
        this.borderThickness = builder.borderThickness;
        this.borderPaint = new Paint();
        this.borderPaint.setColor(Color.WHITE);
        this.borderPaint.setStyle(Style.STROKE);
        this.borderPaint.setAntiAlias(true);
        this.borderPaint.setStrokeWidth((float) this.borderThickness);
        Paint paint = this.getPaint();
        paint.setAntiAlias(true);
        paint.setColor(this.color);
    }

    private int getDarkerShade(int color) {
        return Color.rgb((int) (0.9F * (float) Color.red(color)),
                (int) (0.9F * (float) Color.green(color)),
                (int) (0.9F * (float) Color.blue(color)));
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        Rect r = this.getBounds();
        if (this.borderThickness > 0) {
            this.drawBorder(canvas);
        }

        int count = canvas.save();
        canvas.translate((float) r.left, (float) r.top);
        int width = this.width < 0 ? r.width() : this.width;
        int height = this.height < 0 ? r.height() : this.height;
        int fontSize = this.fontSize < 0 ? Math.min(width, height) / 2
                : this.fontSize;
        this.textPaint.setTextSize((float) fontSize);
        canvas.drawText(this.text, (float) (width / 2), (float) (height / 2)
                - (this.textPaint.descent() + this.textPaint.ascent()) / 2.0F,
                this.textPaint);
        canvas.restoreToCount(count);
    }

    private void drawBorder(Canvas canvas) {
        RectF rect = new RectF(this.getBounds());
        rect.inset((float) (this.borderThickness / 2),
                (float) (this.borderThickness / 2));
        canvas.drawOval(rect, this.borderPaint);

    }

    public void setAlpha(int alpha) {
        this.textPaint.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter cf) {
        this.textPaint.setColorFilter(cf);
    }

    public int getOpacity() {
        return -3;
    }

    public int getIntrinsicWidth() {
        return this.width;
    }

    public int getIntrinsicHeight() {
        return this.height;
    }

    public static ImageViewBorder.IShapeBuilder builder() {
        return new ImageViewBorder.Builder();
    }

    public interface IShapeBuilder {
        ImageViewBorder.IConfigBuilder beginConfig();

        ImageViewBorder.IBuilder round();

        ImageViewBorder buildRound(String var1, int var2);
    }

    public interface IBuilder {
        ImageViewBorder build(String var1, int var2);
    }

    public interface IConfigBuilder {
        ImageViewBorder.IConfigBuilder width(int var1);

        ImageViewBorder.IConfigBuilder height(int var1);

        ImageViewBorder.IConfigBuilder textColor(int var1);

        ImageViewBorder.IConfigBuilder withBorder(int var1);

        ImageViewBorder.IConfigBuilder fontSize(int var1);

        ImageViewBorder.IConfigBuilder bold();

        ImageViewBorder.IConfigBuilder toUpperCase();

        ImageViewBorder.IShapeBuilder endConfig();
    }

    public static class Builder implements ImageViewBorder.IConfigBuilder,
            ImageViewBorder.IShapeBuilder, ImageViewBorder.IBuilder {
        private String text;
        private int color;
        private int borderThickness;
        private int width;
        private int height;
        private Typeface font;
        private RectShape shape;
        public int textColor;
        private int fontSize;
        private boolean isBold;
        private boolean toUpperCase;
        public float radius;

        private Builder() {
            this.text = "";
            this.color = -7829368;
            this.textColor = -1;
            this.borderThickness = 0;
            this.width = -1;
            this.height = -1;
            this.shape = new RectShape();
            this.font = Typeface.create("sans-serif-light", 0);
            this.fontSize = -1;
            this.isBold = false;
            this.toUpperCase = false;
        }

        public ImageViewBorder.IConfigBuilder width(int width) {
            this.width = width;
            return this;
        }

        public ImageViewBorder.IConfigBuilder height(int height) {
            this.height = height;
            return this;
        }

        public ImageViewBorder.IConfigBuilder textColor(int color) {
            this.textColor = color;
            return this;
        }

        public ImageViewBorder.IConfigBuilder withBorder(int thickness) {
            this.borderThickness = thickness;
            return this;
        }

        public ImageViewBorder.IConfigBuilder fontSize(int size) {
            this.fontSize = size;
            return this;
        }

        public ImageViewBorder.IConfigBuilder bold() {
            this.isBold = true;
            return this;
        }

        public ImageViewBorder.IConfigBuilder toUpperCase() {
            this.toUpperCase = true;
            return this;
        }

        public ImageViewBorder.IConfigBuilder beginConfig() {
            return this;
        }

        public ImageViewBorder.IShapeBuilder endConfig() {
            return this;
        }

        public ImageViewBorder.IBuilder round() {
            this.shape = new OvalShape();
            return this;
        }

        public ImageViewBorder buildRound(String text, int color) {
            this.round();
            return this.build(text, color);
        }

        public ImageViewBorder build(String text, int color) {
            this.color = color;
            this.text = text;
            return new ImageViewBorder(this);
        }

    }
}
