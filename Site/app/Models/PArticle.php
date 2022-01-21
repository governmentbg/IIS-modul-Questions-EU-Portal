<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $Ar_id
 * @property int        $ArT_id
 * @property string     $Ar_Title
 * @property string     $Ar_SubTitle
 * @property string     $Ar_Body
 * @property Date       $Ar_Date
 * @property int        $Ar_Timestamp
 * @property int        $Ar_Order
 * @property int        $C_St_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class PArticle extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'P_Article';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'Ar_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'Ar_id', 'ArT_id', 'Ar_Title', 'Ar_SubTitle', 'Ar_Body', 'Ar_Date', 'Ar_Time', 'Ar_Timestamp', 'Ar_Order', 'C_St_id', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];

    /**
     * The attributes that should be casted to native types.
     *
     * @var array
     */
    protected $casts = [
        'Ar_id' => 'int', 'ArT_id' => 'int', 'Ar_Title' => 'string', 'Ar_SubTitle' => 'string', 'Ar_Body' => 'string', 'Ar_Date' => 'date', 'Ar_Timestamp' => 'timestamp', 'Ar_Order' => 'int', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'Ar_Date', 'Ar_Timestamp', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    public $timestamps = false;

    public static function boot()
    {
        parent::boot();

        static::creating(function ($article) {
            // $article->X_User_id = auth()->user()->id;
            $article->created_at = now();
            $article->updated_at = now();
        });

        static::saving(function ($article) {
            // $article->X_User_id = auth()->user()->id;
            $article->updated_at = now();
        });
    }

    // Scopes...

    // Functions ...

    // Relations ...

    public function eq_type()
    {
        return $this->belongsTo(PArticleType::class, 'ArT_id');
    }

    public function eq_doc()
    {
        return $this->hasMany(PArticleDocuments::class, 'Ar_id');
    }
}
