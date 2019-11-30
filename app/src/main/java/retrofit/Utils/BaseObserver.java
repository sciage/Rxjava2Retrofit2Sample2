package retrofit.Utils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Data return unified processing https://www.jianshu.com/p/ff619fea7e22
 * @param <T>
 */
public abstract class BaseObserver<T> implements Observer<BaseResponse<T>> {
    private static final String TAG = "BaseObserver";
    @Override
    public void onNext(BaseResponse<T> response) {
        //Here is the unified processing of the basic data. For example:
        if(response.getRes_code()==200){
            onSuccess(response.getDemo());
        }else{
            onFailure(null,response.getErr_msg());
        }
    }

    @Override
    public void onError(Throwable e) {//服务器错误信息处理
        onFailure(e, RxExceptionUtil.exceptionHandler(e));
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {
    }

    public abstract void onSuccess(T result);

    public abstract void onFailure(Throwable e,String errorMsg);

}
