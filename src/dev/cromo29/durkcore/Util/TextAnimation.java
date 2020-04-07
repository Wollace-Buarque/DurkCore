package dev.cromo29.durkcore.Util;


import com.google.common.collect.Lists;

import java.util.List;

public class TextAnimation {
    private AnimationType animationType;

    public TextAnimation(AnimationType animationType) {
        this.animationType = animationType;
    }

    public void addFrame(int times, String frame, Object... args) {
        animationType.addFrame(times, frame, args);
    }

    public void addFrames(String... frames) {
        animationType.addFrames(frames);
    }

    public void setFrame(int framePosition, String frame, Object... args) {
        animationType.setFrame(framePosition, frame, args);
    }

    public void removeFrame(int framePosition) {
        animationType.removeFrame(framePosition);
    }

    public void clearFrames() {
        animationType.clearFrames();
    }

    public String getOriginalText() {
        return animationType.getOriginalText();
    }

    public int getColorPosition() {
        return animationType.getColorPosition();
    }

    public int getFramePosition() {
        return animationType.getFramePosition();
    }

    public String next() {
        return animationType.next();
    }

    public boolean hasNext() {
        return animationType.hasNext();
    }

    private abstract static class AnimationType {
        public int position;
        public int framePosition;
        public List<String> frames;
        public String str;
        public String colorBefore;
        public String colorAfter;
        public String colorMid;
        public String textColor;
        public String originalStr;

        private AnimationType() {
            frames = Lists.newArrayList();
        }

        public void addFrame(int times, String frame, Object... args) {
            for (int i = 0; i < times; ++i)
                frames.add(TXT.parse(frame, args));
        }

        public void addFrames(String... frames) {
            for (String frame : frames)
                addFrame(1, frame);
        }

        public void setFrame(int framePosition, String frame, Object... args) {
            if (framePosition >= frames.size())
                addFrame(1, frame, args);
             else
                frames.set(framePosition, TXT.parse(frame, args));
        }

        public void removeFrame(int framePosition) {
            frames.remove(framePosition);
        }

        public void clearFrames() {
            frames.clear();
        }

        public String getOriginalText() {
            return originalStr;
        }

        public int getColorPosition() {
            return position;
        }

        public int getFramePosition() {
            return framePosition;
        }

        public abstract String next();

        public abstract boolean hasNext();
    }

    public static class ColorScrollForward extends AnimationType {
        public ColorScrollForward(String text, String textColor, String colorBefore, String colorMid, String colorAfter) {
            this.originalStr = text;
            this.str = text;
            this.colorMid = TXT.parse(colorMid);
            this.colorBefore = TXT.parse(colorBefore);
            this.colorAfter = TXT.parse(colorAfter);
            this.textColor = TXT.parse(textColor);
            this.position = -1;
        }

        @Override
        public boolean hasNext() {
            return position <= str.length();
        }

        @Override
        public String next() {
            if (position == str.length()) {
                ++position;
                return textColor + str.substring(0, str.length() - 1) + colorBefore + str.substring(str.length() - 1);
            }

            if (position > str.length())
                position = -1;

            if (position <= -1) {
                if (!frames.isEmpty()) {
                    if (framePosition < frames.size()) {
                        ++framePosition;
                        return frames.get(framePosition - 1);
                    }
                    framePosition = 0;
                }
                ++position;
                return colorAfter + str.substring(0, 1) + textColor + str.substring(1);
            }

            if (position == 0) {
                String one = str.substring(0, 1);
                String two = colorMid + one;
                String fin = two + colorAfter + str.substring(1, 2) + textColor + str.substring(2);
                ++position;
                return fin;
            }

            String one = str.substring(0, position);
            String two = str.substring(position + 1);
            String three = colorMid + str.substring(position, position + 1);
            int m = one.length();
            int l = two.length();
            String first = (m <= 1) ? (colorBefore + one) : (one.substring(0, one.length() - 1) + colorBefore + one.substring(one.length() - 1));
            String second = (l <= 1) ? (colorAfter + two) : (colorAfter + two.substring(0, 1) + textColor + two.substring(1));
            String fin2 = textColor + first + three + second;

            ++position;
            return fin2;
        }
    }

    public static class ColorScrollBackwards extends AnimationType {
        public ColorScrollBackwards(String text, String textColor, String colorBefore, String colorMid, String colorAfter) {
            this.originalStr = text;
            this.str = text;
            this.colorMid = TXT.parse(colorMid);
            this.colorBefore = TXT.parse(colorBefore);
            this.colorAfter = TXT.parse(colorAfter);
            this.textColor = TXT.parse(textColor);
            this.position = this.str.length();
        }

        @Override
        public boolean hasNext() {
            return position >= -1;
        }

        @Override
        public String next() {
            if (position < -1)
                position = str.length();

            if (position >= str.length()) {
                if (!frames.isEmpty()) {
                    if (framePosition < frames.size()) {
                        ++framePosition;
                        return frames.get(framePosition - 1);
                    }
                    framePosition = 0;
                }
                --position;
                return textColor + str.substring(0, str.length() - 1) + colorBefore + str.substring(str.length() - 1);
            }

            if (position == str.length() - 1) {
                String one = colorBefore + str.substring(str.length() - 1);
                String two = colorMid + str.substring(str.length() - 2, str.length() - 1);
                String fin = textColor + str.substring(0, str.length() - 2) + two + one;
                --position;
                return fin;
            }

            if (position == 1) {
                String fin2 = colorBefore + str.substring(0, 1) + colorMid + str.substring(1, 2) + colorAfter + str.substring(2, 3) + textColor + str.substring(3);
                --position;
                return fin2;
            }

            if (position == 0) {
                String fin2 = colorMid + str.substring(0, 1) + colorAfter + str.substring(1, 2) + textColor + str.substring(2);
                --position;
                return fin2;
            }

            if (position == -1) {
                String fin2 = colorAfter + str.substring(0, 1) + textColor + str.substring(1);
                --position;
                return fin2;
            }

            String one = str.substring(0, position);
            String two = str.substring(position + 1);
            String three = colorMid + str.substring(position, position + 1);
            int m = one.length();
            int l = two.length();
            String first = (m <= 1) ? (colorBefore + one) : (one.substring(0, one.length() - 1) + colorBefore + one.substring(one.length() - 1));
            String second = (l <= 1) ? (colorAfter + two) : (colorAfter + two.substring(0, 1) + textColor + two.substring(1));
            String fin3 = this.textColor + first + three + second;

            --position;
            return fin3;
        }
    }

    public static class FillColorForward extends AnimationType {
        public FillColorForward(String text, String textColor, String colorBefore, String colorMid, String colorAfter) {
            this.originalStr = text;
            this.str = text;
            this.colorMid = TXT.parse(colorMid);
            this.colorBefore = TXT.parse(colorBefore);
            this.colorAfter = TXT.parse(colorAfter);
            this.textColor = TXT.parse(textColor);
            this.position = -1;
        }

        @Override
        public boolean hasNext() {
            return position <= str.length();
        }

        @Override
        public String next() {
            if (position == str.length()) {
                ++position;
                return colorBefore + str;
            }

            if (position > str.length())
                position = -1;

            if (position <= -1) {
                if (!frames.isEmpty()) {
                    if (framePosition < frames.size()) {
                        ++framePosition;
                        return frames.get(framePosition - 1);
                    }
                    framePosition = 0;
                }
                ++position;
                return colorAfter + str.substring(0, 1) + textColor + str.substring(1);
            }

            if (position == 0) {
                String one = str.substring(0, 1);
                String two = colorMid + one;
                String fin = two + colorAfter + str.substring(1, 2) + textColor + str.substring(2);
                ++position;
                return fin;
            }

            String one = str.substring(0, position);
            String two = str.substring(position + 1);
            String three = colorMid + str.substring(position, position + 1);
            int m = one.length();
            int l = two.length();
            String first = (m <= 1) ? one : (one.substring(0, one.length() - 1) + one.substring(one.length() - 1));
            String second = (l <= 1) ? (colorAfter + two) : (colorAfter + two.substring(0, 1) + textColor + two.substring(1));
            String fin2 = colorBefore + first + three + second;

            ++position;
            return fin2;
        }
    }

    public static class FillColorBackwards extends AnimationType {
        public FillColorBackwards(String text, String textColor, String colorBefore, String colorMid, String colorAfter) {
            this.originalStr = text;
            this.str = text;
            this.colorMid = TXT.parse(colorMid);
            this.colorBefore = TXT.parse(colorBefore);
            this.colorAfter = TXT.parse(colorAfter);
            this.textColor = TXT.parse(textColor);
            this.position = this.str.length();
        }

        @Override
        public boolean hasNext() {
            return position >= -1;
        }

        @Override
        public String next() {
            if (position < -1)
                position = str.length();

            if (position >= str.length()) {
                if (!frames.isEmpty()) {
                    if (framePosition < frames.size()) {
                        ++framePosition;
                        return frames.get(framePosition - 1);
                    }
                    framePosition = 0;
                }
                --position;
                return textColor + str.substring(0, str.length() - 1) + colorBefore + str.substring(str.length() - 1);
            }

            if (position == str.length() - 1) {
                String one = colorBefore + str.substring(str.length() - 1);
                String two = colorMid + str.substring(str.length() - 2, str.length() - 1);
                String fin = textColor + str.substring(0, str.length() - 2) + two + one;
                --position;
                return fin;
            }

            if (position == 1) {
                String fin2 = colorBefore + str.substring(0, 1) + colorMid + str.substring(1, 2) + colorAfter + str.substring(2);
                --position;
                return fin2;
            }
            if (position == 0) {
                String fin2 = colorMid + str.substring(0, 1) + colorAfter + str.substring(1);
                --position;
                return fin2;
            }

            if (position == -1) {
                String fin2 = colorAfter + str;
                --position;
                return fin2;
            }

            String one = str.substring(0, position);
            String two = str.substring(position + 1);
            String three = colorMid + str.substring(position, position + 1);
            int m = one.length();
            int l = two.length();
            String first = (m <= 1) ? (colorBefore + one) : (one.substring(0, one.length() - 1) + colorBefore + one.substring(one.length() - 1));
            String second = (l <= 1) ? (colorAfter + two) : (colorAfter + two);
            String fin3 = textColor + first + three + second;

            --position;
            return fin3;
        }
    }

    public static class ColorBlink extends AnimationType {
        public ColorBlink(int blinks, int blinkTime, String text, String firstColor, String secondColor, boolean endWithFirstColor) {
            framePosition = 0;
            for (int i = 0; i < blinks; ++i) {
                addFrame(blinkTime, firstColor + text);
                addFrame(blinkTime, secondColor + text);
            }

            if (endWithFirstColor)
                addFrame(blinks, firstColor + text);
        }

        @Override
        public boolean hasNext() {
            return framePosition < frames.size();
        }

        @Override
        public String next() {
            if (framePosition >= frames.size())
                framePosition = 0;

            if (framePosition < frames.size())
                ++framePosition;

            return frames.get(framePosition - 1);
        }
    }

    public static class Hypixel extends AnimationType {
        private TextAnimation fillColorForward;
        private TextAnimation colorBlink;

        public Hypixel(String text, String textColor, String colorBefore, String colorMid, String colorAfter, int blinks, int blinkTime, String firstColor, String secondColor, int lastBlinkTime) {
            this.originalStr = text;
            this.str = text;
            this.colorMid = TXT.parse(colorMid);
            this.colorBefore = TXT.parse(colorBefore);
            this.colorAfter = TXT.parse(colorAfter);
            this.textColor = TXT.parse(textColor);
            this.position = -1;
            this.fillColorForward = new TextAnimation(new FillColorForward(text, textColor, colorBefore, colorMid, colorAfter));
            (this.colorBlink = new TextAnimation(new ColorBlink(blinks, blinkTime, text, firstColor, secondColor, true))).addFrame(lastBlinkTime, firstColor + text);
        }

        @Override
        public boolean hasNext() {
            return fillColorForward.hasNext() || colorBlink.hasNext();
        }

        @Override
        public String next() {
            if (fillColorForward.hasNext())
                return fillColorForward.next();

            if (colorBlink.hasNext())
                return colorBlink.next();

            colorBlink.next();
            return fillColorForward.next();
        }
    }
}