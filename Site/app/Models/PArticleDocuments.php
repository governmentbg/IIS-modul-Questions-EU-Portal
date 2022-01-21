<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $ArD_id
 * @property int        $Ar_id
 * @property string     $ArD_Title
 * @property string     $ArD_Filename
 * @property string     $ArD_Type
 * @property string     $ArD_Size
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class PArticleDocuments extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'P_Article_Documents';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'ArD_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'ArD_id', 'Ar_id', 'ArD_Title', 'ArD_Filename', 'ArD_Type', 'ArD_Size', 'created_at', 'updated_at', 'deleted_at'
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
        'ArD_id' => 'int', 'Ar_id' => 'int', 'ArD_Title' => 'string', 'ArD_Filename' => 'string', 'ArD_Type' => 'string', 'ArD_Size' => 'string', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'created_at', 'updated_at', 'deleted_at'
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



    public function eq_doc()
    {
        return $this->belongsTo(PArticle::class, 'Ar_id');
    }
}
