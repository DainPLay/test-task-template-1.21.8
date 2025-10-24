package net.dainplay.protobuf;

public final class MessagesProto {
    private MessagesProto() {}
    
    public static final class Message {
        private String text;
        
        public Message() {}
        
        public Message(String text) {
            this.text = text;
        }
        
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        
        public byte[] toByteArray() {
            try {
                return text.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            } catch (Exception e) {
                return new byte[0];
            }
        }
        
        public static Message parseFrom(byte[] data) {
            try {
                String text = new String(data, java.nio.charset.StandardCharsets.UTF_8);
                return new Message(text);
            } catch (Exception e) {
                return new Message();
            }
        }
    }
}