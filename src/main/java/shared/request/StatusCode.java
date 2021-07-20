package shared.request;

public enum StatusCode {
    BAD_REQUEST {
        public int getCode() {
            return 400;
        }
    },
    OK{
        public int getCode() {
            return 200;
        }
    },
    CREATED{
        public int getCode() {
          return 201;
      }
    },
    INTERNAL_SERVER_ERROR{
        public int getCode() {
            return 500;
        }
    },
    NOT_FOUND{
        public int getCode(){
            return 404;
        }
    };

    public abstract int getCode();
}
